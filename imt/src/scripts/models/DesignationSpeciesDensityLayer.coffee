define [
  "cs!models/GridLayer"
  "cs!helpers/Globals"
], (GridLayer, Globals) -> GridLayer.extend
  defaults:
    opacity: 1
    resolution: "auto"
    isPolygon: false
    datasets: []

  initialize: () ->
    @set "wms", Globals.gis "DesignationSpeciesDensity/#{@attributes.code}"
    @set "name", @attributes.name
    GridLayer.prototype.initialize.call(this, arguments); #call super initialize

  getAvailableDatasetsURL: -> Globals.api "designations/#{@attributes.code}/datasets"