define [
  "cs!models/GridLayer"
  "cs!models/Dataset"
  "cs!helpers/Globals"
], (GridLayer, Dataset, Globals) -> GridLayer.extend
  defaults:
    entityType: 'taxondataset'
    opacity: 1
    resolution: "auto"
    isPolygon: false

  url: -> Globals.api "taxonDatasets/#{@id}"

  idAttribute: "key"

  initialize: () ->
    @set "name", @attributes.title

    @on 'change:startDate change:endDate', -> @trigger 'change:wms'
    GridLayer.prototype.initialize.call(this, arguments); #call super initialize

  getWMS: -> Globals.gis "DatasetSpeciesDensity/#{@id}",
    startDate : @get "startDate"
    endDate : @get "endDate"

  getLegendIcon: ->
    background: "linear-gradient(to right, #ffff80 0%, #76130a 100%)"

  getUsedDatasets: -> [ new Dataset @attributes ]

  ###
  Define what this layer is mapping. Only ever species richness
  ###
  mapOf:-> "species richness"