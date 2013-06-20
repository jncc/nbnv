define [
  "cs!models/Layer"
  "cs!helpers/Globals"
  "hbs!templates/slds/Default"
], (Layer, Globals, sld) -> Layer.extend
  defaults:
    opacity: 1
    colour: "FF0000"

  initialize: () ->
    @set "wms", Globals.gis "SingleSpecies/#{@attributes.ptaxonVersionKey}"
    @set "name", @attributes.name
    @set "layer", 'Grid-10km'

    @on 'change:colour change:layer', => @trigger 'change:sld'

  getSLD: -> sld
    layer: @getLayer(),
    colour: @get "colour"