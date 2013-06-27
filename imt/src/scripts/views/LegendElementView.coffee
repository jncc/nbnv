define [
  "backbone"
  "cs!views/LayerCustomisationView"
  "hbs!templates/LegendElement"
], (Backbone, LayerCustomisationView, template) -> Backbone.View.extend

  initialize: ->
    @$el.addClass "ui-state-default"
    @$el.html template
      name: @model.getName()

    do @updateLegendIcon
    
    @listenTo @model, 'change:legendIcon', @updateLegendIcon
    @listenTo @model, 'change:name', @updateDescription

    # Add a click listener to the icon
    @$('span.icon').click =>
      new LayerCustomisationView model: @model

  updateLegendIcon:->
    @$('span.icon').css @model.getLegendIcon()

  updateDescription:->
    @$('p.description').html @model.getName()