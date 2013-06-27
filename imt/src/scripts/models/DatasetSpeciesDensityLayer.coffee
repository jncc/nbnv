define [
  "cs!models/GridLayer"
  "cs!models/Dataset"
  "cs!models/mixins/TemporalFilterMixin"
  "cs!helpers/Globals"
], (GridLayer, Dataset, TemporalFilterMixin, Globals) -> GridLayer.extend _.extend {}, TemporalFilterMixin,
  defaults:
    entityType: 'taxondataset'
    opacity: 1
    resolution: "auto"
    isPolygon: false
    startDate: TemporalFilterMixin.earliestRecordDate
    endDate: TemporalFilterMixin.latestRecordDate

  url: -> Globals.api "taxonDatasets/#{@id}"

  idAttribute: "key"

  initialize: () ->
    @set "name", @attributes.title

    @on 'change:startDate change:endDate', -> @trigger 'change:wms'
    GridLayer.prototype.initialize.call(this, arguments); #call super initialize
    TemporalFilterMixin.initialize.call(this, arguments); #initalize the mixin

  getWMS: -> Globals.gis "DatasetSpeciesDensity/#{@id}",
    startDate : @getStartDate() #Temporal mixin handles this value
    endDate : @getEndDate() #Temporal mixin handles this value

  getLegendIcon: ->
    background: "linear-gradient(to right, #ffff80 0%, #76130a 100%)"

  getUsedDatasets: -> [ new Dataset @attributes ]

  ###
  Define what this layer is mapping. Only ever species richness
  ###
  mapOf:-> "species richness"