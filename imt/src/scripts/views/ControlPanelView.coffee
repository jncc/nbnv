define [
  "backbone"
  "hbs!templates/ControlPanel"
  "cs!views/LegendView"
  "cs!views/DatasetsView"
  "cs!views/PickerView"
  "jquery.ui.tabs"
  "jquery.ui.effect-slide"
], (Backbone, controlPanel, LegendView, DatasetsView, PickerView) -> Backbone.View.extend
  events :
    "tabsactivate" : "updateIsPicking"
    "click .hideControlPanel button" : "hideControlPanel"

  initialize:->
    do @render
    do @updatePickerState
    @updateVisiblity(0) # update state for the first time
    
    @listenTo @model, 'change:controlPanelVisible', @updateVisiblity
    @listenTo @model.getPicker(), 'change:hasResults', @updateIsPicking
    @listenTo @model.getLayers(), 'add remove reset', @updatePickerState
    @listenTo @model.getLayers(), 'add', @activateLegend

  render: ->
    @$el.html controlPanel()
    @$('.controlPanelTabs').tabs() #Turn the control panel into jquery tabs

    @legendView = new LegendView
      collection: @model.getLayers()
      el: @$('.legend')

    @datasetsView = new DatasetsView
      collection: @model.getLayers()
      el: @$('.datasets')

    @pickerView = new PickerView
      model: @model
      el: @$('.picker')

  hideControlPanel :-> @model.set "controlPanelVisible", false

  updateVisiblity: (speed)->
    options = direction: 'right', duration :speed, effect: 'slide'
    if @model.get "controlPanelVisible" then @$el.show options else  @$el.hide options

  updatePickerState:->
    state = if @model.getPicker().getPickableLayers().length is 0 then "disable" else "enable"
    @$('.controlPanelTabs').tabs state, 2

  activateLegend: ->
    @$('.controlPanelTabs').tabs("option", "active", 0)

  ###
  Determine if the picker tab has been selected. If it has
  put the map into picking mode
  ###
  updateIsPicking: ->
    isOnPickerTab = @$('.controlPanelTabs').tabs( "option", "active" ) is 2 #picker is on second tab
    @model.getPicker().set "isPicking", isOnPickerTab and @model.getCurrentUser().isLoggedIn()

    if isOnPickerTab and @model.getPicker().hasResults()
      @$el.animate width:800
    else
      @model.getPicker().clearResults()
      @$el.animate width:357