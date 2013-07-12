define [
  'backbone'
  'cs!helpers/Globals' 
], (Backbone, Globals) -> Backbone.Collection.extend 

  url: Globals.api "taxonObservations"
