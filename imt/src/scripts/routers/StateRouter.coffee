define [
  'jquery'
  'underscore'
  'backbone'
  'cs!models/SingleSpeciesLayer'
  'cs!models/DatasetSpeciesDensityLayer'
  'cs!models/DesignationSpeciesDensityLayer'
  'cs!models/HabitatLayer'
  'cs!models/SiteBoundaryLayer'
  'cs!helpers/Numbers'
  'cs!helpers/representations/BaseLayers'
  'cs!helpers/representations/Keys'
  'cs!helpers/representations/Resolutions'
  'cs!helpers/representations/Styles'
  'cs!helpers/representations/Years'
  'cs!helpers/representations/Viewports'
], ($, _, Backbone, SingleSpeciesLayer, DatasetSpeciesDensityLayer, DesignationSpeciesDensityLayer, HabitatLayer, SiteBoundaryLayer, Numbers, BaseLayers, Keys, Resolutions, Styles, Years, Viewports) -> Backbone.Router.extend
  routes:
    "*data" : "updateModel"

  initialize: (options) ->
    @model = options.model
    @listenTo @model, 'change:viewport', _.debounce @updateRoute, 2000
    @listenTo @model, 'change:baseLayer', @updateRoute
    @listenTo @model.getLayers(), 'add remove position change:colour change:usedDatasets change:startDate change:endDate change:resolution', @updateRoute

  ###
  Update the url fragment to represent the current state of this router
  If the url already has a fragment, then the current one is left in place.
  ###
  refresh: -> 
    if not Backbone.history.fragment
      router.navigate router.getCurrentRoute(), replace:true

  updateModel:(route)->
    if route?
      state = @getStateFromRoute(route) #load the state from the url
      @model.set _.omit(state, 'layers'), routing:true
      layers = @model.getLayers()

      $.when
        .apply($, _.map state.layers, (layer) -> do layer.fetch)  #Fetch the layers
        .then(-> layers.reset state.layers, routing: true)        #Reset the apps layers to the new layers

  updateRoute: (changed..., options) -> @navigate @getCurrentRoute() if not options.routing

  ###
  This router handles the conversion of the state of the map to a minimal url safe string
  representation. The url should represent :
  * The current map viewport
  * The Background Layer Selected
  * The Layers and their respective state (filters, styling etc)

  From the above we can propose a simple structure for the url:
    [BACKGROUND_LAYER][VIEWPORT]![LAYER_1]![LAYER_2]...![LAYER_N]

  There are two types of layer which we add to the map; Observation and Context layers.
  To simplify to url encoding scheme, the layers which fall into each type are handled by
  the same logic. When processing each layer component, it is necessary to be able to 
  distinguish what type of layer it is we need to create/serialize. This layer type is represented
  in the first 3 bits of the Base64 didgit of the Layer def, such that the bit mask 0b000111
  will obtain in. This allows for 8 different Layer Types, at the time of writting there are only 5.

  The since we are only using the first 3 bits of the first Base64 digit to represent the type of layer,
  there are a spare 3 bits which can be used for layer customisation.
  ###


  ###
  Observation layers

  An observation layer component is composed in the form :
    [RESOLUTIONS][KEY][,STYLE][,YEAR][,DATASET_1]...[,DATASET_N]

  In the above format, the 'STYLE' and 'YEAR' parameters are optional, there presence
  is dictated in the 3 bit options for this layer. In a observation layer the options
  number represents :
    0b100 - reserved
    0b010 - style filter
    0b001 - year filter

  The following is the general code for reading this type of component
  ###
  parseObservationLayer: (layerType, layerDef, options) ->
    filterOptions = @parseFilterOptions options #read the filter options
    parts = layerDef.substring(1).split ','

    layerConfig = Resolutions.expandResolution layerDef[0]

    #Load the key for this type of layer and set it to this layers idAttribute
    layerConfig[layerType.constr.prototype.idAttribute] = layerType.expandKey parts.shift() 
    _.extend layerConfig, Styles.expandStyle parts.shift() if filterOptions.styleFilter
    _.extend layerConfig, Years.expandYearRange parts.shift() if filterOptions.yearFilter

    layerConfig.datasets = _.map parts, (key) => Keys.expandDatasetKey key
    new layerType.constr layerConfig

  ###
  As already stated, the 3 bit options for an observation layer dictate if styling or
  year filtering is being applied. We can read this out from the number using:
  ###
  parseFilterOptions: (options) -> 
    styleFilter: (options & 0x02) is 0x02
    yearFilter: (options & 0x01) is 0x01

  ###
  To write it
  ###
  miniFilterOptions: (layer) ->
    options = 0
    options += 0x01 if layer.isYearFiltering()
    options += 0x02 if not layer.isDefaultAppearance()
    return options

  ###
  That defines how we can read the observation layers, we now need to be able to write
  out the string to match the format above.
  ###
  shrinkObservationLayer: (layerType, layer) -> 
    attr = layer.attributes
    layerParts = ["#{Resolutions.shrinkResolution(attr)}#{layerType.shrinkKey(layer.id)}"]
    layerParts.push "#{Styles.shrinkStyle(attr)}" if not layer.isDefaultAppearance()
    layerParts.push "#{Years.shrinkYearRange(attr)}" if layer.isYearFiltering()
    layerParts = layerParts.concat _.map attr.datasets, (key) => Keys.shrinkDatasetKey key

    options: @miniFilterOptions layer
    layerDef: layerParts.join ','

  ###
  Next we come on to the simpler types of layers, the context layers. These layers are represented 
  in the form:
    [KEY][,STYLE]

  The 3bit options are not used for these types of layer. If a style is to be applied, this will be detected
  by the presence of a ',' in the component.

  The following is the general code for reading this type of component 
  ###
  parseContextLayer: (layerType, layerDef, options) ->
    parts = layerDef.split ','
    layerConfig = {} #Create somewhere to put the configuration to build
    layerConfig[layerType.constr.prototype.idAttribute] = layerType.expandKey parts[0] 
    _.extend layerConfig, Styles.expandStyle parts[1] if parts.length is 2

    return new layerType.constr layerConfig

  ###
  And then writing this component
  ###
  shrinkContextLayer: (layerType, layer) ->
    attr = layer.attributes
    layerParts = ["#{layerType.shrinkKey(layer.id)}"]
    layerParts.push "#{Styles.shrinkStyle(attr)}" if not layer.isDefaultAppearance()

    options: 0,
    layerDef: layerParts.join ','

  ###
  At this point we have all the parts in place to be able to parse the different types
  of layers. However these functions require the definitions of the layers which we
  can read and write along with their respective constructor, key reader/writer methods 
  and a link to the correct shrinking/parsing function. We can define these in an array.

  The position of the definition in the array will dictate the 3 bit value to associate 
  with the layer
  ###
  layerTypes: [
    {isObservation: true, constr: SingleSpeciesLayer, expandKey: Keys.expandTVK, shrinkKey: Keys.shrinkTVK }
    {isObservation: true, constr: DatasetSpeciesDensityLayer, expandKey: Keys.expandDatasetKey, shrinkKey: Keys.shrinkDatasetKey }
    {isObservation: true, constr: DesignationSpeciesDensityLayer, expandKey:((key) -> key), shrinkKey:((key) -> key)}
    {isObservation: false, constr: HabitatLayer, expandKey: Keys.expandDatasetKey, shrinkKey: Keys.shrinkDatasetKey}
    {isObservation: false, constr: SiteBoundaryLayer, expandKey: Keys.expandDatasetKey, shrinkKey: Keys.shrinkDatasetKey}
  ]

  ###
  We now have an understanding of all the different types of layers and how to read/
  write a string representation of them. To generate this state url, we can interrogate
  the @model to get the layers, viewport and background layer to create this state url.

  To do this:
  ###
  getCurrentRoute: ->
    baseLayerChar = BaseLayers.shrinkBaseLayer @model.get 'baseLayer'
    viewport = Viewports.shrinkViewport @model.get 'viewport'
    layers = @model
      .getLayers()
      .map((layer) => 
        #Find the correct shrinker and shrink
        layerType = _.find(@layerTypes, (type) -> layer instanceof type.constr)
        shrinker = if layerType.isObservation then @shrinkObservationLayer else @shrinkContextLayer
        shrunkLayer = shrinker.call @, layerType, layer #shrink in the context of the router
        #Create the control digit
        layerControl = shrunkLayer.options * 8 + _.indexOf @layerTypes, layerType
        Numbers.toBase64(layerControl) + shrunkLayer.layerDef #concat to layerDef
      )
      .join '!'

    "#{baseLayerChar}#{viewport}" + if layers then "!#{layers}" else ''

  ###
  We can invert this logic to get the viewport, background layer and layers definitions from
  a given route. Note that the layers will be represented by their Backbone.Model, but will 
  need to be fetched before applying to the map
  ###
  getStateFromRoute: (route) ->
    parts = route.substring(1).split('!') #remove the baseLayer character and split around !

    baseLayer: BaseLayers.expandBaseLayer route.substring 0, 1
    viewport: Viewports.expandViewport parts[0]
    layers: _.map parts.slice(1), (layerDef) => 
        layerDefDigit = Numbers.fromBase64 layerDef[0]
        layerOptions = Math.floor layerDefDigit / 8 #Get the 3 high order bits
        layerType = layerDefDigit & 0x07 #Get the 3 low order bits
        parser = if @layerTypes[layerType].isObservation then @parseObservationLayer else @parseContextLayer
        parser.call @, @layerTypes[layerType], layerDef.substring(1), layerOptions