define [
  "backbone"
  "cs!views/LayerCustomisationView"
  "tpl!templates/LegendElement.tpl"
], (Backbone, LayerCustomisationView, template) -> Backbone.View.extend

  events:
    "click button.customise": "customise"
    "click button.destroy": "removeLayer"

  initialize: ->
    do @render
    
    @listenTo @model, 'change:legendIcon', @updateLegendIcon
    @listenTo @model, 'change:name', @updateDescription
    @listenTo @model, 'change:visibility', @updateVisibility

  customise:->
    if not @customisationView?
      @customisationView = new LayerCustomisationView model: @model
    else
      do @customisationView.reopen

  removeLayer: -> @model.collection.remove @model

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

  ###
  This is called when the legend element view has been notified
  to be removed. Therefore remove the customisationView if one
  has been created
  ###
  remove: ->
    @customisationView.remove() if @customisationView?
    Backbone.View.prototype.remove.call this, arguments #Then call the super method