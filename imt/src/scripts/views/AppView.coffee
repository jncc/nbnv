define [
  "jquery"
  "backbone"
  "hbs!templates/AppScaffolding"
  "cs!views/OpenLayersView"
  "cs!views/LoadingView"
  "cs!views/SearchView"
  "cs!views/BaseLayerSelectorView"
  "cs!views/ControlPanelView"
  "cs!views/UserView"
  "cs!helpers/Globals"
], ($, Backbone, imtScaffolding, OpenLayersView, LoadingView, SearchView, BaseLayerSelectorView, ControlPanelView, UserView, Globals) -> Backbone.View.extend
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
    @listenTo @model, 'change:baseLayer', @styleTermsLink

  showControlPanel:-> @model.set "controlPanelVisible", true

  renderShowControlPanelBtn:-> 
    ele = @$('.showControlPanel button')
    if @model.get 'controlPanelVisible' then do ele.hide else do ele.show

  styleTermsLink: ->
    @$('.termsconditions')
      .removeClass(@model.getBaseLayers().join ' ')
      .addClass(@model.get 'baseLayer')

  render: ->
    @$el.html imtScaffolding 
      termsLink: Globals.portal "Terms"
      currYear: new Date().getFullYear()

    @openlayersView = new OpenLayersView
      model: @model
      el: @$('.openlayers')

    @loadingView = new LoadingView
      model: @model
      el: @$('.loading-status')

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
    do @styleTermsLink