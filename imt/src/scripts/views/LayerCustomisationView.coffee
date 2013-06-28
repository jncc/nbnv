define [
  "jquery"
  "backbone"
  "cs!views/ColourSelectorView"
  "cs!views/OpacityView"
  "cs!views/ResolutionSelectorView"
  "cs!views/TemporalFilterView"
  "cs!views/DatasetSelectorView"
], ($, Backbone, ColourSelectorView, OpacityView, ResolutionSelectorView, TemporalFilterView, DatasetSelectorView) -> Backbone.View.extend
  ###
  The role of this view is to determine which sub customisation
  views are required for the given model that has been provided
  to this instance
  ###  
  initialize:->
    @$el.addClass "layerCustomisation"

    if @model.isStyleable
      @colorView = new ColourSelectorView
        model: @model
        el: $('<div>').appendTo @$el

    @opacityView = new OpacityView
      model: @model
      el: $('<div>').appendTo @$el

    if @model.isGridLayer
      @resolutionView = new ResolutionSelectorView
        model: @model
        el: $('<div>').appendTo @$el

    if @model.isTemporalFilterable
      @resolutionView = new TemporalFilterView
        model: @model
        el: $('<div>').appendTo @$el

    if @model.isDatasetFilterable
      @resolutionView = new DatasetSelectorView
        collection: @model.availableDatasets
        el: $('<div>').appendTo @$el

    @$el.dialog
      title: @model.getName()
      resizable: false
      width: 400
      close: -> $(this).dialog('destroy').remove() #remove the dialog from the dom on close