define [
  "cs!models/Layer",
  "cs!helpers/Globals"
], (Layer, Globals) -> Layer.extend
  defaults:
    opacity: 1

  initialize: ()->
    @set "wms", Globals.gis "SingleSpecies/#{@attributes.ptaxonVersionKey}"
    @set "name", @attributes.title
    @set "layers", ['Grid-10km']