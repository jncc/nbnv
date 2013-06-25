define [
  "underscore"
  "cs!models/GridLayer"
  "cs!models/DatasetFilterMixin"
  "cs!helpers/Globals"
], (_, GridLayer, DatasetFilterMixin, Globals) -> GridLayer.extend _.extend {}, DatasetFilterMixin,
  defaults:
    opacity: 1
    resolution: "auto"
    isPolygon: false
    datasets: []

  initialize: () ->
    @set "wms", Globals.gis "DesignationSpeciesDensity/#{@attributes.code}"
    @set "name", @attributes.name
    GridLayer.prototype.initialize.call(this, arguments); #call super initialize
    DatasetFilterMixin.initialize.call(this, arguments); #initalize the mixin
  
  getLegendIcon: ->
    background: "linear-gradient(to right, #ffff80 0%, #76130a 100%)"
  
  ###
  Get the url which lists the datasets which provide data for this map
  Needed by the DatasetFilterMixin
  ###
  getAvailableDatasetsURL: -> Globals.api "designations/#{@attributes.code}/datasets"