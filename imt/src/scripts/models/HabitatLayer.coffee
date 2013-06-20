define [
  "cs!models/Layer"
  "cs!helpers/Globals"
  "hbs!templates/slds/Default"
], (Layer, Globals, sld) -> Layer.extend
  defaults: 
    opacity: 1
    wms: Globals.gis "HabitatDatasets"

  initialize: ()->
    @set "name", @attributes.title
    @set "layer", @attributes.key

    @on 'change:colour change:layer', => @trigger 'change:sld'

  getSLD: -> sld
    layer: @getLayer(),
    colour: @get "colour"