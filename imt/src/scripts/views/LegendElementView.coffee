define [
  "backbone"
  "cs!views/LayerCustomisationView"
  "hbs!templates/LegendElement"
], (Backbone, LayerCustomisationView, template) -> Backbone.View.extend

  events:
    "click span.icon": "customise"
    "click button.destroy": "removeLayer"

  initialize: ->
    do @render
    
    @listenTo @model, 'change:legendIcon', @updateLegendIcon
    @listenTo @model, 'change:name', @updateDescription
    @listenTo @model, 'change:visibility', @updateVisibility

  customise:->
    new LayerCustomisationView model: @model

  removeLayer:-> @model.collection.remove @model

  updateLegendIcon:->
    @$('span.icon').css @model.getLegendIcon()

  updateDescription:->
    @$('p.description').html @model.getName()

  updateVisibility:->
    if @model.isVisible()
      @$el.removeClass "ui-state-disabled"
    else
      @$el.addClass "ui-state-disabled"

  render: ->
    @$el.addClass "ui-state-default"
    @$el.html template
      name: @model.getName()

    do @updateLegendIcon
    do @updateVisibility