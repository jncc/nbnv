define [
  "backbone"
], (Backbone) -> Backbone.View.extend

  initialize: ->
    @$el.append '<p>Test layer :)</p>'