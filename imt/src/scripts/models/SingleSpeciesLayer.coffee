define [
  "cs!models/GridLayer"
  "cs!helpers/Globals"
  "hbs!templates/slds/Default"
], (GridLayer, Globals, sld) -> GridLayer.extend
  defaults:
    opacity: 1
    colour: "FF0000"
    resolution: "auto"
    isPresence: true
    isPolygon: false

  initialize: () ->
    @set "name", @attributes.name

    @on 'change:colour change:layer', => @trigger 'change:sld'
    @on 'change:isPresence', => @trigger 'change:wms'
    GridLayer.prototype.initialize.call(this, arguments); #call super initialize

  getSLD: -> sld
    layer: @getLayer(),
    colour: @get "colour"

  getWMS: -> Globals.gis "SingleSpecies/#{@attributes.ptaxonVersionKey}?abundance=" +
    if @attributes.isPresence then "presence" else "absence"
