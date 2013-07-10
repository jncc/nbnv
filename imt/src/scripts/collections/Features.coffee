define [
  'backbone' 
  'cs!models/Feature'
], (Backbone, Feature) -> Backbone.Collection.extend 
  model: Feature
