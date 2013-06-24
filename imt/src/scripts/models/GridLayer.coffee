define [
  "underscore"
  "cs!models/Layer"
], (_, Layer) -> Layer.extend
  ###
  Flag that this is a grid layer and should be updated by the App
  when the auto resolution changes
  ###
  isGridLayer: true
  
  initialize: () ->
    @_availableDatasets = new Backbone.Collection [], url: @getAvailableDatasetsURL()
    
    @listenTo @_availableDatasets, 'sync', @updateAvailableDatasets
    @listenTo @_availableDatasets, 'change:selected', @updateSelectedDatasets

    @on 'change:resolution change:isPolygon change:autoResolution', => do @generateLayer
    do @getDatasets #Prepopulate the datasets list

  ###
  Listen to when a available datasets list has been populated and update
  the selected value on each model
  ###
  updateAvailableDatasets: ->
    selectedDatasets = @get "datasets"
    allSelected = _.isEmpty selectedDatasets
    @_availableDatasets.forEach (dataset) -> 
      dataset.set 'selected',
          allSelected or _.contains( selectedDatasets, dataset.attributes.key),
          silent: true # don't notify that we have updated

  ###
  Listener for when the selected value of the available dataset has changed, 
  update the selected datasets accordingly. When all are selected the 
  the selected datasets array should be empty
  ###
  updateSelectedDatasets:->
    selectedDatasets = @_availableDatasets
      .chain()
      .filter( (dataset) -> dataset.get "selected")
      .map( (dataset) -> dataset.get "key" )
      .value() 

    allSelected = selectedDatasets.length is @_availableDatasets.length
    @set "datasets", if allSelected then [] else selectedDatasets

  ###
  Get the list of datasets which contribute to this GridLayer.
  This method will perform a fetch on the _availableDatasets and then
  return a jQuery promise of the Backbone.Collection
  ###
  getDatasets:->
    if @_availableDatasetsFetch? then return @_availableDatasetsFetch
    defer = $.Deferred()
    @_availableDatasets.fetch success: => defer.resolve(@_availableDatasets)
    @_availableDatasetsFetch = defer.promise()

  ###
  Generate the wms layer to request depending on the state of this GridLayer
  ###
  generateLayer: ->
    resolution = @get "resolution"
    layer = if @get "isPolygon" then "None-Grid" else 
            if resolution is 'auto' then "Grid-#{@get 'autoResolution'}"
            else "Grid-#{resolution}"
    @set 'layer', layer