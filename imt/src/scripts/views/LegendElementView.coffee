define [
  "backbone"
  "cs!views/LayerCustomisationView"
], (Backbone, LayerCustomisationView) -> Backbone.View.extend

  initialize: ->
    @$el.append "<p>#{@model.getName()}</p>"
    new LayerCustomisationView
      model: @model
      el: $('<div>').appendTo $('body')
