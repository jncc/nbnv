define [
  "backbone"
  "cs!views/LegendElementView"
  "jquery-ui"
], (Backbone, LegendElementView, $) -> Backbone.View.extend

  initialize: ->
    @listenTo @collection, 'add', @addLayer
    @listenTo @collection, 'remove', @removeLayer
    @$el.sortable
      start: (event, ui) => 
        @_oldPosition = (@collection.length - 1) - ui.item.index()
      update: (event, ui) =>
        newPosition = (@collection.length - 1) - ui.item.index()
        @collection.position @_oldPosition, newPosition
    @$el.disableSelection()

  addLayer: (layer) -> 
    layer._legendElementView = new LegendElementView
      model: layer
      el: $('<li></li>').prependTo(@$el)

  removeLayer: (layer) -> do layer._legendElementView.remove