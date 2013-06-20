define [
  "backbone"
  "cs!views/LegendElementView"
  "jquery-ui"
], (Backbone, LegendElementView, $) -> Backbone.View.extend

  initialize: ->
    @listenTo @collection, 'add', @addLayer
    @$el.sortable
      start: (event, ui) => 
        @_oldPosition = ui.item.index()
      update: (event, ui) =>
        @_newPosition = ui.item.index()
        @collection.position @_oldPosition, @_newPosition
    @$el.disableSelection()

  addLayer: (layer) -> 
    new LegendElementView
      model: layer
      el: $('<li></li>').appendTo(@$el)