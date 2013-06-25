define [
  "cs!models/GridLayer"
  "cs!models/Dataset"
  "cs!helpers/Globals"
], (GridLayer, Dataset, Globals) -> GridLayer.extend
  defaults:
    opacity: 1
    resolution: "auto"
    isPolygon: false

  initialize: () ->
    @set "wms", Globals.gis "DatasetSpeciesDensity/#{@attributes.key}"
    @set "name", @attributes.title
    GridLayer.prototype.initialize.call(this, arguments); #call super initialize

  getLegendIcon: ->
    background: "linear-gradient(to right, #ffff80 0%, #76130a 100%)"

  getUsedDatasets: -> [ new Dataset @attributes ]