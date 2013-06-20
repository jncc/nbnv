define [
  "backbone"
], (Backbone) -> Backbone.Model.extend
  getSLD:-> @get "sld"
  getName:-> @get "name"
  getWMS:-> @get "wms"
  getLayer:-> @get "layer"
  getOpacity: -> @get "opacity"