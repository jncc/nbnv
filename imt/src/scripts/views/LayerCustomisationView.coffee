define [
  "jquery"
  "backbone"
  "cs!views/ColourView"
  "cs!views/OpacityView"
  "cs!views/ResolutionView"
  "cs!views/TemporalView"
  "cs!views/DatasetSelectorView"
  "hbs!templates/LayerCustomisation"
], ($, Backbone, ColourView, OpacityView, ResolutionView, TemporalView, DatasetSelectorView, customisationTemplate) -> Backbone.View.extend
  ###
  The role of this view is to determine which sub customisation
  views are required for the given model that has been provided
  to this instance
  ###  
  initialize:->
    @$el.html customisationTemplate(@model)

    if @model.isStyleable
      @colorView = new ColourView
        model: @model
        el: @$('.colourPicker')

    @opacityView = new OpacityView
      model: @model
      el: @$('.opacity')

    if @model.isGridLayer
      @resolutionView = new ResolutionView
        model: @model
        el: @$('.resolution')

    if @model.isTemporalFilterable
      @resolutionView = new TemporalView
        model: @model
        el: @$('.temporal')

    if @model.isDatasetFilterable
      @resolutionView = new DatasetSelectorView
        model: @model
        el: @$('.datasets')

    @$el.dialog
      title: @model.getName()
      resizable: false
      width: 400
      close: -> $(this).dialog('destroy').remove() #remove the dialog from the dom on close

