define [
  "backbone"
  "cs!views/LayerCustomisationView"
  "hbs!templates/LegendElement"
], (Backbone, LayerCustomisationView, template) -> Backbone.View.extend

  initialize: ->
    @$el.html template
      name: @model.getName()

    do @updateLegendIcon
    
    @listenTo @model, 'change:legendIcon', @updateLegendIcon

    # Add a click listener to the icon
    @$('span.icon').click =>
      new LayerCustomisationView model: @model

  updateLegendIcon:->
    @$('span.icon').css @model.getLegendIcon()