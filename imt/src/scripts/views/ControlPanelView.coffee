define [
  "jquery-ui",
  "backbone",
  "hbs!templates/ControlPanel"
  "cs!views/LegendView",
], ($, Backbone, controlPanel, LegendView) -> Backbone.View.extend
  
  initialize:->
    do @render
    do @updateVisiblity # update state for the first time

    @listenTo @model, 'change:controlPanelVisible', @updateVisiblity

  render: ->
    @$el.html controlPanel()
    @$el.tabs(); #Turn the control panel into jquery tabs

    @legendView = new LegendView
      model: @model,
      el: $('.legend', @$el)

  updateVisiblity:->
    if @model.get "controlPanelVisible" then do @$el.show else do @$el.hide
