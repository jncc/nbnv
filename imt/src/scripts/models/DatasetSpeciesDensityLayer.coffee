define [
  "cs!models/GridLayer"
  "cs!helpers/Globals"
], (GridLayer, Globals) -> GridLayer.extend
  defaults:
    opacity: 1
    resolution: "auto"
    isPolygon: false

  initialize: () ->
    @set "wms", Globals.gis "DatasetSpeciesDensity/#{@attributes.key}"
    @set "name", @attributes.title
    GridLayer.prototype.initialize.call(this, arguments); #call super initialize