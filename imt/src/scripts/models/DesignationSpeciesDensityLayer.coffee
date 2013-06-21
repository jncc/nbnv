define [
  "cs!models/Layer"
  "cs!helpers/Globals"
], (Layer, Globals) -> Layer.extend
  defaults:
    opacity: 1

  initialize: () ->
    @set "wms", Globals.gis "DesignationSpeciesDensity/#{@attributes.code}"
    @set "name", @attributes.name
    @set "layer", 'Grid-10km'