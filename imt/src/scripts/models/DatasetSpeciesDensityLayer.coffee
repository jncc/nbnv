define [
  "cs!models/Layer"
  "cs!helpers/Globals"
], (Layer, Globals) -> Layer.extend
  defaults:
    opacity: 1

  initialize: () ->
    @set "wms", Globals.gis "DatasetSpeciesDensity/#{@attributes.key}"
    @set "name", @attributes.title
    @set "layer", 'Grid-10km'