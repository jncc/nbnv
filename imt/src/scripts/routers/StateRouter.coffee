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
  'cs!helpers/representations/Keys'
  'cs!helpers/representations/Resolutions'
  'cs!helpers/representations/Styles'
  'cs!helpers/representations/Years'
], ($, _, Backbone, SingleSpeciesLayer, DatasetSpeciesDensityLayer, DesignationSpeciesDensityLayer, HabitatLayer, SiteBoundaryLayer, Numbers, Keys, Resolutions, Styles, Years) -> Backbone.Router.extend
  routes:
    "*data" : "updateModel"

  layerTypes: []

  initialize: (options) ->
    @model = options.model

    @layerTypes.push constr: HabitatLayer, parser: @parseHabitatLayer, shrinker: @miniHabitatLayer
    @layerTypes.push constr: SiteBoundaryLayer, parser: @parseSiteBoundaryLayer, shrinker: @miniSiteBoundaryLayer
    @layerTypes.push constr: SingleSpeciesLayer, parser: @parseSingleSpeciesLayer, shrinker: @miniSingleSpeciesLayer
    @layerTypes.push constr: DatasetSpeciesDensityLayer, parser: @parseDatasetsSpeciesDensityLayer, shrinker: @miniDatasetsSpeciesDensityLayer
    @layerTypes.push constr: DesignationSpeciesDensityLayer, parser: @parseDesignationSpeciesDensityLayer, shrinker: @miniDesignationSpeciesDensityLayer

    @listenTo @model.getLayers(), 'add remove position change:colour change:usedDatasets change:startDate change:endDate change:resolution', @updateRoute

  updateModel:(route)->
    if route?
      sites = _.map route.split('!'), (layerDef) => @parseLayerDef layerDef
      layers = @model.getLayers()

      $.when
        .apply($, _.map sites, (layer) -> do layer.fetch)
        .then(-> layers.reset sites, routing: true)


  updateRoute: (changed..., options) ->
    if not options.routing
      @navigate @model
                  .getLayers()
                  .map((layer) => 
                    #Find the correct shrinker and shrink
                    layerType = _.find(@layerTypes, (type) -> layer instanceof type.constr)
                    shrunkLayer = layerType.shrinker.call this, layer #shrink in the context of the router
                    #Create the control rixit
                    layerControl = shrunkLayer.options * 8 + _.indexOf @layerTypes, layerType
                    Numbers.toBase64(layerControl) + shrunkLayer.layerDef #concat to layerDef
                  )
                  .join '!'

  ###
  There are currently 5 different types of Layer which can be added to the map:
  * HabitatLayer - 0b000
  * SiteBoundaryLayer - 0b001
  * SingleSpeciesLayer - 0b010
  * DatasetSpeciesDensityLayer - 0b011
  * DesignationSpeciesDensityLayer - 0b100

  The different type of layers are represented in low order of bits of the first
  base64 character of the layerDef. This leaves the 3 high order bits available
  for customisation of the layer. See the relevant function to find the definition
  in the correct context.
  ###
  parseLayerDef: (layerDef) ->
    layerDefNumber = Numbers.fromBase64 layerDef[0]
    layerOptions = Math.floor layerDefNumber / 8 #Get the 3 high order bits
    layerType = layerDefNumber & 0x03 #Get the 3 low order bits
    @layerTypes[layerType].parser.call @, layerDef.substring(1), layerOptions


  ###
  @param layerDef The definition for this site boundary layer, emiting the control charater
  ###
  parseSiteBoundaryLayer: (layerDef, options) ->
    parts = layerDef.split(',')
    style = @parseStyle parts[1] if parts.length is 2
    new SiteBoundaryLayer _.extend key: @parseDatasetKey(parts[0]), style

  ###
  The layer definition of the single species takes the following form :
  [CONFIG_RIXIT][MINI_TVK][,STYLE][,YEAR][,DATASET_1]...[,DATASET_N]
  The presence of the style and year filters is dictated by the given options
  3 bit number:
  0b100 - reserved
  0b010 - style filter
  0b001 - year filter
  ###
  parseSingleSpeciesLayer: (layerDef, options) ->
    filterOptions = @parseFilterOptions options
    parts = layerDef.substring(1).split ','

    layerConfig = Resolutions.expandResolution layerDef.substring 0, 1
    layerConfig.ptaxonVersionKey = Keys.expandTVK parts.shift()

    _.extend layerConfig, Styles.expandStyle parts.shift() if filterOptions.styleFilter
    _.extend layerConfig, Years.expandYearRange parts.shift() if filterOptions.yearFilter

    layerConfig.datasets = _.map parts, (key) => Keys.expandDatasetKey key

    new SingleSpeciesLayer layerConfig

  miniSingleSpeciesLayer: (layer) -> 
    attr = layer.attributes
    layerParts = ["#{Resolutions.shrinkResolution(attr)}#{Keys.shrinkTVK(layer.id)}"]
    layerParts.push "#{Styles.shrinkStyle(attr)}" if layer.isUsingCustomColour() or layer.getOpacity() isnt 1
    layerParts.push "#{Years.shrinkYearRange(attr)}" if layer.isYearFiltering()
    layerParts = layerParts.concat _.map attr.datasets, (key) => Keys.shrinkDatasetKey key

    options: @miniFilterOptions layer
    layerDef: layerParts.join ','

  parseFilterOptions: (options) -> 
    styleFilter: (options & 0x02) is 0x02
    yearFilter: (options & 0x01) is 0x01

  miniFilterOptions: (layer) ->
    options = 0
    options += 0x01 if layer.isYearFiltering()
    options += 0x02 if layer.isUsingCustomColour?() or layer.getOpacity() isnt 1
    return options

  ###
  This router handles the conversion of the state of the map to a minimal url safe string
  representation. The url should represent :
  * The current map viewport
  * The Background Layer Selected
  * The Layers and their respective state (filters, styling etc)

  From the above we can propose a simple structure for the url:
    [VIEWPORT][BACKGROUND_LAYER][LAYER_1]![LAYER_2]...![LAYER_N]

  There are two types of layer which we add to the map; Observation and Context layers.
  To simplify to url encoding scheme, the layers which fall into each type are handled by
  the same logic. When processing each layer component, it is necessary to be able to 
  distinguish what type of layer it is we need to create/serialize. This layer type is represented
  in the first 3 bits of the Base64 didgit of the Layer def, such that the bit mask 0b000111
  will obtain in.

  The 3 bits of the layer allow for upto 8 different layer definitions 

  First lets look at the Observation Layers.

  Observation layers (SingleSpeciesLayer, DatasetSpeciesLayer, DesignationSpeciesLayer) can be 
  represented
  ###

