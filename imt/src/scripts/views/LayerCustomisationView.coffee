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
      $('<h5>').html('What colour should the layer be?').appendTo @$el
      @colorView = new ColourSelectorView
        model: @model
        el: $('<div>').appendTo @$el

    if @model.isGridLayer
      $('<h5>').html('Which grid should records be mapped at?').appendTo @$el
      @resolutionView = new ResolutionSelectorView
        model: @model
        el: $('<div>').appendTo @$el

    if @model.isTemporalFilterable
      $('<h5>').html('Which years would you like to see records from?').appendTo @$el
      @temporalView = new TemporalFilterView
        model: @model
        el: $('<div>').appendTo @$el

    if @model.isDatasetFilterable
      $('<h5>').html('Which datasets should be used?').appendTo @$el
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