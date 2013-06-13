define [
  "backbone"
], (Backbone) -> Backbone.Model.extend
  getName:-> @get "name"
  getWMS:-> @get "wms"
  getLayers:-> @get "layers"
  getOpacity: -> @get "opacity"