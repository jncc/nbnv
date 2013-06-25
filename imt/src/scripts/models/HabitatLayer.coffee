define [
  "cs!models/Layer"
  "cs!models/Dataset"
  "cs!helpers/Globals"
  "hbs!templates/slds/Default"
], (Layer, Dataset, Globals, sld) -> Layer.extend
  defaults: 
    opacity: 1
    wms: Globals.gis "HabitatDatasets"

  initialize: ()->
    @set "name", @attributes.title
    @set "layer", @attributes.key

    @on 'change:colour', -> @trigger 'change:legendIcon'
    @on 'change:colour change:layer', -> @trigger 'change:sld'

  getLegendIcon: ->
    backgroundColor: "#" + @get "colour"

  getSLD: -> sld
    layer: @getLayer(),
    colour: @get "colour"

  getUsedDatasets: -> [ new Dataset @attributes ]