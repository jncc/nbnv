define [
  "backbone",
  "hbs!templates/ControlPanel"
  "cs!views/LegendView"
], (Backbone, controlPanel, LegendView) -> Backbone.View.extend
  
  initialize:->
    @render()

  render: ->
    @$el.html controlPanel()
    @controlPanelView = new LegendView
      model: @model,
      el: $('.legend', @$el)