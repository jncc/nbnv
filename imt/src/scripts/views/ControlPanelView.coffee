define [
  "jquery-ui"
  "backbone"
  "hbs!templates/ControlPanel"
  "cs!views/LegendView"
  "cs!views/DatasetsView"
  "cs!views/PickerView"
], ($, Backbone, controlPanel, LegendView, DatasetsView, PickerView) -> Backbone.View.extend
  
  initialize:->
    do @render
    do @updateVisiblity # update state for the first time

    @listenTo @model, 'change:controlPanelVisible', @updateVisiblity

  render: ->
    @$el.html controlPanel()
    @$el.tabs() #Turn the control panel into jquery tabs

    @legendView = new LegendView
      collection: @model.getLayers()
      el: @$('.legend')

    @datasetsView = new DatasetsView
      collection: @model.getLayers()
      el: @$('.datasets')

    @pickerView = new PickerView
      collection: @model.getPicker().getFeatures()
      el: @$('.picker');

  updateVisiblity:->
    if @model.get "controlPanelVisible" then do @$el.show else do @$el.hide
