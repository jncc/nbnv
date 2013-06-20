define [
  "backbone"
  "cs!views/LegendElementView"
  "jquery-ui"
], (Backbone, LegendElementView, $) -> Backbone.View.extend

  initialize: ->
    @listenTo @collection, 'add', @addLayer
    @$el.sortable
      start: (ui, event) ->
      update:0
    @$el.disableSelection()

  addLayer: (layer) -> 
    new LegendElementView
      model: layer
      el: $('<li></li>').appendTo(@$el)