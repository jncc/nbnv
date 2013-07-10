define [
  'underscore'
  'backbone' 
  'cs!collections/Layers'
  'cs!collections/Features'
], (_, Backbone, Layers, Features) -> Backbone.Model.extend 
  defaults :
    features: new Features
    isPicking: false
    layers: new Layers

  getLayers: -> @get "layers"

  getFeatures: -> @get "features"

  updateFeatures: ->
    #logic to update features
    