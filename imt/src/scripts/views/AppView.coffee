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
    "click .showControlPanel button" : "showControlPanel"
    
  ###
  Register to imt events which the view should respond to
  ###
  initialize: ->
    @$el.addClass "interactiveMapTool"
    do @render

    @listenTo @model, 'change:controlPanelVisible', @renderShowControlPanelBtn

  showControlPanel :-> @model.set "controlPanelVisible", true

  renderShowControlPanelBtn:-> 
    ele = @$('.showControlPanel button')
    if @model.get 'controlPanelVisible' then do ele.hide else do ele.show

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

    do @renderShowControlPanelBtn #update the state of the control panel button