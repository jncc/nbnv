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
    startDate: 1600
    endDate: new Date().getFullYear()
    datasets: []


  initialize: () ->
    @set "name", @attributes.name

    @on 'change:colour change:layer', => @trigger 'change:sld'
    @on 'change:isPresence change:startDate change:endDate change:datasets', => @trigger 'change:wms'
    GridLayer.prototype.initialize.call(this, arguments); #call super initialize

  getSLD: -> sld
    layer: @getLayer(),
    colour: @get "colour"

  getWMS: -> Globals.gis "SingleSpecies/#{@attributes.ptaxonVersionKey}",
    abundance: if @attributes.isPresence then "presence" else "absence"
    startDate : @get "startDate"
    endDate : @get "endDate"
    datasets: @get("datasets").join ','

  getAvailableDatasetsURL: -> Globals.api "taxa/#{@attributes.ptaxonVersionKey}/datasets"
