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
    @listenTo @model.getPicker(), 'change:hasResults', @updateIsPicking

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
  updateIsPicking: ->
    isOnPickerTab = @$el.tabs( "option", "active" ) is 2 #picker is on second tab
    @model.getPicker().set "isPicking", isOnPickerTab

    if isOnPickerTab and @model.getPicker().hasResults()
      @$el.animate width:800
    else
      @$el.animate width:350