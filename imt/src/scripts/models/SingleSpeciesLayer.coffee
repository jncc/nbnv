define [
  "underscore"
  "cs!models/GridLayer"
  "cs!models/mixins/DatasetFilterMixin"
  "cs!models/mixins/PolygonFillMixin"
  "cs!helpers/Globals"
], (_, GridLayer, DatasetFilterMixin, PolygonFillMixin, Globals) -> GridLayer.extend _.extend {}, DatasetFilterMixin, PolygonFillMixin,
  defaults:
    opacity: 1
    resolution: "auto"
    isPresence: true
    isPolygon: false
    startDate: 1600
    endDate: new Date().getFullYear()
    datasets: []

  url: -> Globals.api "taxa/#{@id}"
  
  idAttribute: "ptaxonVersionKey"

  initialize: () ->
    do @updateSymbolToUse
    
    @on 'change:isPresence', @updateSymbolToUse
    @on 'change:isPresence change:startDate change:endDate change:datasets', -> @trigger 'change:wms'
    GridLayer.prototype.initialize.call(this, arguments); #call super initialize
    DatasetFilterMixin.initialize.call(this, arguments); #initalize the mixin
    PolygonFillMixin.initialize.call(this, arguments); #initalize the mixin

  getWMS: -> Globals.gis "SingleSpecies/#{@id}",
    abundance: if @attributes.isPresence then "presence" else "absence"
    startDate : @get "startDate"
    endDate : @get "endDate"
    datasets: @get("datasets").join ','

  ###
  Define what this layer is mapping
  ###
  mapOf:-> 
    type = if @get "isPresence" then "occurrence" else "absence"
    "#{type} records"

  ###
  Workout which symbol to use and then set as the symbol attribute
  ###
  updateSymbolToUse:-> @set "symbol", if @get "isPresence" then "fill" else "hatching"

  ###
  Get the url which lists the datasets which provide data for this map
  Needed by the DatasetFilterMixin
  ###
  getAvailableDatasetsURL: -> Globals.api "taxa/#{@id}/datasets"