define [
  "cs!models/Layer",
  "cs!helpers/Globals"
], (Layer, Globals) -> Layer.extend
  defaults:
    opacity: 1
    wms: Globals.gis "SiteBoundaryDatasets"

  initialize: ()->
    @set "name", @attributes.title
    @set "layers", [@attributes.key]