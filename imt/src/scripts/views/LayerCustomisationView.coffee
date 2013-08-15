define [
  "jquery"
  "backbone"
  "cs!views/ColourSelectorView"
  "cs!views/ResolutionSelectorView"
  "cs!views/TemporalFilterView"
  "cs!views/DatasetSelectorView"
], ($, Backbone, ColourSelectorView, ResolutionSelectorView, TemporalFilterView, DatasetSelectorView) -> Backbone.View.extend
  ###
  The role of this view is to determine which sub customisation
  views are required for the given model that has been provided
  to this instance
  ###  
  initialize:->
    @$el.addClass "layerCustomisation"
    
    do @render

    @listenTo @model, 'change:name', => @$el.dialog "option", "title", @model.getName()

  render:->
    if @model.isStyleable
      @colorView = new ColourSelectorView
        model: @model
        el: $('<div>').appendTo @$el

    if @model.isGridLayer
      @resolutionView = new ResolutionSelectorView
        model: @model
        el: $('<div>').appendTo @$el

    if @model.isTemporalFilterable
      @temporalView = new TemporalFilterView
        model: @model
        el: $('<div>').appendTo @$el

    if @model.isDatasetFilterable
      @datasetsView = new DatasetSelectorView
        collection: @model.availableDatasets
        el: $('<div>').appendTo @$el

    @$el.dialog
      title: @model.getName()
      resizable: false
      width: 400
      buttons: 
        Ok: => do @update; @$el.dialog( "close" )
        Update: => do @update
      
  update:->
    do @colorView.apply if @colorView
    do @temporalView.apply if @temporalView
    do @datasetsView.apply if @datasetsView

  ###
  Reopens the view and brings it back in to focus
  ###
  reopen:->
    @$el.dialog('open')
        .dialog('moveToTop')

  ###
  Override the remove method for the view and clean up the
  .dialog
  ###
  remove:->
    @$el.dialog('destroy').remove()
    Backbone.View.prototype.remove.call this, arguments