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
    @render()

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