define [
  "backbone",
], (Backbone) -> Backbone.View.extend

  initialize: ->
    @listenTo @model.getLayers(), 'add', @addLayer

  addLayer: (layer) -> 
    @$el.append '<p>Test layer</p>'
    
