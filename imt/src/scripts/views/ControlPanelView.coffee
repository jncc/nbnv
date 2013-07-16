define [
  "jquery-ui"
  "backbone"
  "hbs!templates/ControlPanel"
  "cs!views/LegendView"
  "cs!views/DatasetsView"
  "cs!views/PickerView"
], ($, Backbone, controlPanel, LegendView, DatasetsView, PickerView) -> Backbone.View.extend
  events :
    "tabsactivate" : "updateIsPicking"

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
      model: @model.getPicker()
      el: @$('.picker')

  updateVisiblity:->
    if @model.get "controlPanelVisible" then do @$el.show else do @$el.hide

  ###
  Determine if the picker tab has been selected. If it has
  put the map into picking mode
  ###
  updateIsPicking: (evt, ui)->
    isPicking = ui.newPanel.hasClass "picker"
    @model.getPicker().set "isPicking", isPicking