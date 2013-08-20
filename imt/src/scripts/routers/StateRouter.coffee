define [
  'jquery'
  'underscore'
  'backbone'
  'cs!models/SingleSpeciesLayer'
  'cs!models/DatasetSpeciesDensityLayer'
  'cs!models/DesignationSpeciesDensityLayer'
  'cs!models/HabitatLayer'
  'cs!models/SiteBoundaryLayer'
], ($, _, Backbone, SingleSpeciesLayer, DatasetSpeciesDensityLayer, DesignationSpeciesDensityLayer, HabitatLayer, SiteBoundaryLayer) -> Backbone.Router.extend
  routes:
    "*data" : "updateModel"

  initialize: (options) ->
    @model = options.model
    @listenTo @model.getLayers(), 'add remove position', @updateRoute

  updateModel:(route)->
    if route?
      sites = _.map route.split('!'), (layerDef)=> @parseLayerDef layerDef
      layers = @model.getLayers()
      
      $.when
        .apply($, _.map sites, (layer) -> do layer.fetch)
        .then(-> layers.reset sites, routing: true)

  updateRoute: (layer, collection, options) ->
    #if not options.routing
      #state = @model.getLayers().pluck('key').join ','
      #@navigate state

  ###
  There are currently 5 different types of Layer which can be added to the map:
  * HabitatLayer - 0b000
  * SiteBoundaryLayer - 0b001
  * SingleSpeciesLayer - 0b010
  * DatasetSpeciesDensityLayer - 0b011
  * DesignationSpeciesDensityLayer - 0b100

  The different type of layers are represented in low order of bits of the first
  base64 character of the layerDef. These leaves the 3 high order bits available
  for customisation of the layer. See the relevant function to find the definition
  in the correct context.
  ###
  parseLayerDef: (layerDef) ->
    layerTypes =
      0 : @parseHabitatLayer
      1 : @parseSiteBoundaryLayer
      2 : @parseSingleSpeciesLayer
      3 : @parseDatasetsSpeciesDensityLayer
      4 : @parseDesignationSpeciesDensityLayer

    layerDefNumber = @toNumber layerDef[0]
    layerOptions = Math.floor layerDefNumber / 8 #Get the 3 high order bits
    layerType = layerDefNumber & 0x03 #Get the 3 low order bits
    layerTypes[layerType].call @, layerDef.substring(1), layerOptions

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

    layerConfig = @parseLayerConfig layerDef.substring 0, 1
    layerConfig.ptaxonVersionKey = @parseTaxonVersionKey parts.shift()

    _.extend layerConfig, @parseStyle parts.shift() if filterOptions.styleFilter
    _.extend layerConfig, @parseYearRange parts.shift() if filterOptions.yearFilter

    layerConfig.datasets = _.map parts, (key) => @parseDatasetKey key

    new SingleSpeciesLayer layerConfig

  parseFilterOptions: (options) -> styleFilter: false, yearFilter: false

  #This list can hold up to 8 values
  tvkProviders : ['NHM','NBN','BMS','EHS']

  ###
  7 rixits maximum
  ###
  parseTaxonVersionKey: (miniTVK) ->
    return miniTVK if miniTVK.length is 16 # If the representation is 16 chars long, treat as normal key

    lastCharater = miniTVK.substring(miniTVK.length - 1)
    provider = @toNumber(lastCharater) & 0x07
    numeric = Math.floor((@toNumber miniTVK) / 8)
    @tvkProviders[provider] + "SYS" + @leadingZeros numeric.toString(), 10

  miniTaxonVersionKey: (tvk) ->
    return tvk if not /(NHM|NBN|BMS|EHS)[0-9]{10}/.test tvk #Check if the tvk is minifiable

    type = _.indexOf @tvkProviders, tvk.substring(0, 3) #get the 3bit provider
    numeric = parseInt tvk.substring(3)
    @fromNumber numeric * 8 + type

  datasetTypes : ['HL','GA','SB']

  ###
  The 2 low order bits of the minDatasetKey represent the type of dataset key,
  these are defined as:
  * HL - 0b00
  * GA - 0b01
  * SB - 0b10
  In the case of legacy, the high order bits will be looked up and resolved from
  the legacy array. The others will convert the highorder bits to a numeric value
  which will at max be 6 digits. This will then be appended (with trailing zeros)
  to its respective prefix.
  ###
  parseDatasetKey: (miniDatasetKey) ->
    return miniDatasetKey if miniDatasetKey.length is 8  #If the representation is 8 chars long, treat as normal key

    lastCharater = miniDatasetKey.substring(miniDatasetKey.length - 1)
    type = @toNumber(lastCharater) & 0x03 #get type
    numeric = Math.floor((@toNumber miniDatasetKey) / 4)
    @datasetTypes[type] + @leadingZeros numeric.toString(), 6

  miniDatasetKey: (datasetKey) ->
    return datasetKey if not /(GA|HL|SB)[0-9]{6}/.test datasetKey #check if we can shrink the key

    type = _.indexOf @datasetTypes, datasetKey.substring(0, 2) #get the 2 bit dataset type
    numeric = parseInt datasetKey.substring(2)
    @fromNumber numeric * 4 + type

  parseYearRange: (miniYearRangeRixits) ->
    miniYearRange = @toNumber minYearRangeRixits
    startDate: Math.floor miniYearRange / 512 # shift by 9 bits
    endDate: miniYearRange & 0x1FF #mask with last 9 bits

  ###
  The earliest year which can be recorded on the nbn gateway is 1600.
  If we subtract this value from the start year and end year we can represent
  a year range of 1600 - 2112 in 18bits (3 base64 chars)
  ###
  miniYearRange: (yearRange) ->
    miniYearRange = yearRange.startDate * 512 + yearRange.endDate
    @fromNumber miniYearRange

  resolutions: ['10km', '2km', '1km', '100m', 'auto']

  ###
  The selected resolution of a map can either be one of the following: 
  * 10km - 0
  * 2km - 1
  * 1km - 2
  * 100m - 3
  * auto - 4
  * Polygon (inferred from other values) - 5

  The 6 options can be represented as a 3 bit number, however each has an absence flag.
  Therefore the layers to be requested can be represented as a 4 bit number (or one rixit)
  ###
  miniLayerConfig: (layer) ->
    resolution = if layer.isPolygon then 5 else _.indexOf @resolutions, layer.resolution
    @fromNumber resolution * 2 + (1 & layer.isPresence) #shift 1 bit for the presence/absence flag

  parseLayerConfig: (miniResolution) ->
    resolution = Math.floor miniResolution / 2

    isPolygon: resolution is '5'
    isPresence: (miniResolution & 0x1) is 1 #mask to get the presence absence flag, turn to boolean
    resolution: @resolutions[resolution] if resolution isnt 5

  ###
  The style component is represented as a 30 bit number, this is so that it always neatly
  fits into 5 rixits. 8 bits for the r,g,b values and 6 for the opacity level
  ###
  parseStyle: (minStyleRixits) ->
    minValue = @toNumber minStyleRixits
    colour = Math.floor(minValue / 64).toString 16 #Shift 6 bits and hex
    opacity : (minValue & 0x3F) / 63 #mask with last 6 digits, convert to [0..1]
    colour : @leadingZeros colour, 6
  
  ###
  Convert the style to a base64 encoded string
  ###
  miniStyle: (style) ->
    rgb = parseInt style.colour, 16
    opacity = Math.floor(style.opacity * 63)
    @fromNumber rgb * 64 + opacity

  ###
  A simple function to add leading zeros to a String representation of a 
  Number
  ###
  leadingZeros : (str, length) ->
    while str.length < length
      str = '0' + str
    return str

  ###
  Number to base64 implementation taken from here stackoverflow
  http://stackoverflow.com/questions/6213227/fastest-way-to-convert-a-number-to-radix-64-in-javascript
  ###
  _Rixits : "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz+/",
  
  fromNumber : (number) -> 
    throw "Can't represent negative numbers now" if number < 0
    residual = Math.floor number
    result = ''
    while (true)
      rixit = residual % 64
      result = @_Rixits.charAt(rixit) + result
      residual = Math.floor residual / 64
      break if residual is 0
    return result;

  toNumber : (rixits) ->
    result = 0
    rixits = rixits.split ''
    for e in rixits
      result = (result * 64) + @_Rixits.indexOf e
    return result;