define [
  "backbone"
  "cs!views/LegendElementView"
  "jquery-ui"
], (Backbone, LegendElementView, $) -> Backbone.View.extend

  initialize: ->
    @listenTo @collection, 'add', @addLayer
    @$el.sortable
      start: (event, ui) => 
        @_oldPosition = (@collection.length - 1) - ui.item.index()
      update: (event, ui) =>
        newPosition = (@collection.length - 1) - ui.item.index()
        @collection.position @_oldPosition, newPosition
    @$el.disableSelection()

  addLayer: (layer) -> 
    new LegendElementView
      model: layer
      el: $('<li></li>').prependTo(@$el)