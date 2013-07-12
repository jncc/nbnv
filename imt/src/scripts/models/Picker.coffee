define [
  'underscore'
  'backbone' 
  'cs!collections/Layers'
  'cs!collections/Features'
  'cs!models/Feature'
], (_, Backbone, Layers, Features, Feature) -> Backbone.Model.extend 
  defaults :
    features: new Features
    isPicking: false
    layers: new Layers
    wkt: ""

  initialize: -> 
    @on 'change:wkt', @updateFeatures

    
  getLayers: -> @get "layers"

  getFeatures: -> @get "features"

  updateFeatures: ->
    #logic to update features
    console.log @get('wkt')

#    @getFeatures().add new Feature ({layerName: "layer3", info: "feauture1"})

    
    @getLayers().each((layer) => console.log layer.getPickerResults(@get('wkt')))