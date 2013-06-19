define [
  "jquery",
  "backbone",
  "hbs!templates/AppScaffolding",
  "cs!views/OpenLayersView",
  "cs!views/SearchView",
  "cs!views/BaseLayerSelectorView",
  "cs!views/ControlPanelView"
], ($, Backbone, imtScaffolding, OpenLayersView, SearchView, BaseLayerSelectorView, ControlPanelView) -> Backbone.View.extend
  el: '#imt',

  ###
  Register to imt events which the view should respond to
  ###
  initialize: ->
    @$el.addClass "interactiveMapTool"
    do @render

    #Add a click listener to the settings button and toggle the settings visibility
    $('.controlPanelToggle', @$el).click => do @toggleControlPanel

  toggleControlPanel :->
      @model.set "controlPanelVisible", not @model.get "controlPanelVisible"
      $('.controlPanelToggle', @$el).toggleClass 'active'

  render: ->
    @$el.html imtScaffolding()
    @openlayersView = new OpenLayersView
      model: @model,
      el: $('.openlayers', @$el)

    @searchView = new SearchView
      model: @model,
      el: $('.search', @$el)

    @baseLayerSelectorView = new BaseLayerSelectorView
      model: @model,
      el: $('.baseLayers', @$el)

    @controlPanelView = new ControlPanelView
      model: @model,
      el: $('.controlPanel', @$el)

    $('.controlPanelToggle', @$el).addClass 'active' if @model.get 'controlPanelVisible'