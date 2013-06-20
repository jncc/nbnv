define [
  "backbone"
], (Backbone) -> Backbone.View.extend

  initialize: ->
    @$el.append "<p>#{@model.getName()}</p>"