define [
  "backbone"
  "cs!views/LayerCustomisationView"
], (Backbone, LayerCustomisationView) -> Backbone.View.extend

  initialize: ->
    @$el.append '<p>Test layer :)</p>'
    new LayerCustomisationView
      model: @model
      el: $('<div>').appendTo $('body')