define [
  "underscore"
  "cs!models/GridLayer"
  "cs!models/DatasetFilterMixin"
  "cs!helpers/Globals"
], (_, GridLayer, DatasetFilterMixin, Globals) -> GridLayer.extend _.extend {}, DatasetFilterMixin,
  defaults:
    entityType: 'designation'
    opacity: 1
    resolution: "auto"
    isPolygon: false
    datasets: []

  url: -> Globals.api "designations/#{@id}"

  idAttribute: "code"

  initialize: () ->
    @on 'change:startDate change:endDate change:datasets', -> @trigger 'change:wms'
    GridLayer.prototype.initialize.call(this, arguments); #call super initialize
    DatasetFilterMixin.initialize.call(this, arguments); #initalize the mixin
  
  getWMS: -> Globals.gis "DesignationSpeciesDensity/#{@id}",
    startDate : @get "startDate"
    endDate : @get "endDate"
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