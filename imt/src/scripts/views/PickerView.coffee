define [
  "jquery",
  "backbone",
  "hbs!templates/PickerView"
], ($, Backbone, template) -> Backbone.View.extend


  initialize: ->
    do @render
    @listenTo @collection, 'change', @render

  render: -> 
    alert 'hello from PickerView.coffee'
    @$el.html 'picker view'