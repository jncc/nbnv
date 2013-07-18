define [
  "underscore"
  "cs!models/GridLayer"
  "cs!models/mixins/TemporalFilterMixin"
  "cs!models/mixins/DatasetFilterMixin"
  "cs!helpers/Globals"
], (_, GridLayer, TemporalFilterMixin, DatasetFilterMixin, Globals) -> GridLayer.extend _.extend {}, TemporalFilterMixin, DatasetFilterMixin,
  defaults:
    entityType: 'designation'
    opacity: 1
    visibility: true
    resolution: "auto"
    isPolygon: false    
    startDate: TemporalFilterMixin.earliestRecordDate
    endDate: TemporalFilterMixin.latestRecordDate
    datasets: []

  url: -> Globals.api "designations/#{@id}"

  idAttribute: "code"

  initialize: () ->
    @on 'change:startDate change:endDate change:datasets', -> @trigger 'change:wms'
    GridLayer.prototype.initialize.call(this, arguments); #call super initialize
    TemporalFilterMixin.initialize.call(this, arguments); #initalize the mixin
    DatasetFilterMixin.initialize.call(this, arguments); #initalize the mixin
  
  getWMS: -> Globals.gis "DesignationSpeciesDensity/#{@id}",
    startyear : @getStartDate() #Temporal mixin handles this value
    endyear : @getEndDate() #Temporal mixin handles this value
    datasets: @get("datasets").join ','

  getLegendIcon: ->
    background: "linear-gradient(to right, #ffff80 0%, #76130a 100%)"
  
  ###
  Get the url which lists the datasets which provide data for this map
  Needed by the DatasetFilterMixin
  ###
  getAvailableDatasetsURL: -> Globals.api "designations/#{@id}/datasets"

  ###
  Define what this layer is mapping. Only ever species richness
  ###
  mapOf: -> "species richness"