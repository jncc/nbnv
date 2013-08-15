define [
  "jquery",
  "backbone",
  "hbs!templates/AppScaffolding",
  "cs!views/OpenLayersView",
  "cs!views/SearchView",
  "cs!views/BaseLayerSelectorView",
  "cs!views/ControlPanelView"
  "cs!views/UserView"
], ($, Backbone, imtScaffolding, OpenLayersView, SearchView, BaseLayerSelectorView, ControlPanelView, UserView) -> Backbone.View.extend
  el: '#imt',

  events:
    "click .controlPanelToggle" : "toggleControlPanel"
    
  ###
  Register to imt events which the view should respond to
  ###
  initialize: ->
    @$el.addClass "interactiveMapTool"
    do @render

#    @listenTo @model, 'change:controlPanelVisible', @renderControlPanelToggle

  toggleControlPanel :->
      @model.set "controlPanelVisible", not @model.get "controlPanelVisible"

#  renderControlPanelToggle:-> 
#    if @model.get 'controlPanelVisible' 
#      @$('.controlPanelToggle').addClass 'active'
#      @$('.icon-chevron-left').addClass 'icon-chevron-right'
#      @$('.icon-chevron-left').removeClass 'icon-chevron-left'
#    else
#      @$('.controlPanelToggle').removeClass 'active'
#      @$('.icon-chevron-right').addClass 'icon-chevron-left'
#      @$('.icon-chevron-right').removeClass 'icon-chevron-right'

  render: ->
    @$el.html imtScaffolding()
    @openlayersView = new OpenLayersView
      model: @model
      el: @$('.openlayers')

    @searchView = new SearchView
      model: @model
      el: @$('.search')

    @baseLayerSelectorView = new BaseLayerSelectorView
      model: @model
      el: @$('.baseLayers')

    @controlPanelView = new ControlPanelView
      model: @model
      el: @$('.controlPanel')

    @userView = new UserView
      model: @model.getCurrentUser()
      el: @$('.userView')

#    do @renderControlPanelToggle #update the state of the control panel button