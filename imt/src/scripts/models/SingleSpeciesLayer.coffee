define [
  "underscore"
  "cs!models/GridLayer"
  "cs!models/mixins/TemporalFilterMixin"
  "cs!models/mixins/DatasetFilterMixin"
  "cs!models/mixins/PolygonFillMixin"
  "cs!collections/TaxonObservations"
  "cs!helpers/Globals"
], (_, GridLayer, TemporalFilterMixin, DatasetFilterMixin, PolygonFillMixin, TaxonObservations, Globals) -> GridLayer.extend _.extend {}, TemporalFilterMixin, DatasetFilterMixin, PolygonFillMixin,
  defaults:
    opacity: 1
    visibility: true
    resolution: "auto"
    isPresence: true
    isPolygon: false
    startDate: TemporalFilterMixin.earliestRecordDate
    endDate: TemporalFilterMixin.latestRecordDate
    datasets: []

  url: -> Globals.api "taxa/#{@id}"

  idAttribute: "ptaxonVersionKey"

  initialize: () ->
    do @updateSymbolToUse

    @on 'change:isPresence', @updateSymbolToUse
    @on 'change:isPresence', -> @trigger 'change:name'
    @on 'change:isPresence change:startDate change:endDate change:datasets', -> @trigger 'change:wms'
    GridLayer.prototype.initialize.call(this, arguments); #call super initialize
    TemporalFilterMixin.initialize.call(this, arguments); #initalize the mixin
    DatasetFilterMixin.initialize.call(this, arguments); #initalize the mixin
    PolygonFillMixin.initialize.call(this, arguments); #initalize the mixin

  getWMS: -> Globals.gis "SingleSpecies/#{@id}",
    abundance: if @get "isPresence" then "presence" else "absence"
    startyear : @getStartDate() #Temporal mixin handles this value
    endyear : @getEndDate() #Temporal mixin handles this value
    datasets: @get("datasets").join ','

  getTaxonObservations: -> new TaxonObservations [],
    ptvk: @id
    startYear: @getStartDate() #Temporal mixin handles this value
    endYear : @getEndDate() #Temporal mixin handles this value
    datasetKey: @get("datasets")

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