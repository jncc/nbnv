define [
  "underscore"
  "cs!models/GridLayer"
  "cs!models/DatasetFilterMixin"
  "cs!helpers/Globals"
  "hbs!templates/slds/Default"
], (_, GridLayer, DatasetFilterMixin, Globals, sld) -> GridLayer.extend _.extend {}, DatasetFilterMixin,
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

    @on 'change:colour', -> @trigger 'change:legendIcon'
    @on 'change:colour change:layer', -> @trigger 'change:sld'
    @on 'change:isPresence change:startDate change:endDate change:datasets', -> @trigger 'change:wms'
    GridLayer.prototype.initialize.call(this, arguments); #call super initialize
    DatasetFilterMixin.initialize.call(this, arguments); #initalize the mixin

  getSLD: -> sld
    layer: @getLayer(),
    colour: @get "colour"

  getLegendIcon: ->
    backgroundColor: "#" + @get "colour"

  getWMS: -> Globals.gis "SingleSpecies/#{@attributes.ptaxonVersionKey}",
    abundance: if @attributes.isPresence then "presence" else "absence"
    startDate : @get "startDate"
    endDate : @get "endDate"
    datasets: @get("datasets").join ','

  ###
  Get the url which lists the datasets which provide data for this map
  Needed by the DatasetFilterMixin
  ###
  getAvailableDatasetsURL: -> Globals.api "taxa/#{@attributes.ptaxonVersionKey}/datasets"