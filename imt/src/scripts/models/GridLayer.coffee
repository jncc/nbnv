define [
  "underscore"
  "cs!models/Layer"
  "cs!models/AvailableDataset"
], (_, Layer, AvailableDataset) -> Layer.extend
  ###
  Flag that this is a grid layer and should be updated by the App
  when the auto resolution changes
  ###
  isGridLayer: true
  
  initialize: () ->
    @availableDatasets = new Backbone.Collection [], url: @getAvailableDatasetsURL(), model: AvailableDataset
    
    @listenTo @availableDatasets, 'reset', @updateAvailableDatasets
    @listenTo @availableDatasets, 'change:selected', @updateSelectedDatasets
    @listenTo @availableDatasets, 'reset change:selected', -> @trigger 'change:usedDatasets'
    
    @on 'change:resolution change:isPolygon change:autoResolution', -> do @generateLayer
    
    do @fetchAvailableDatasets #Prepopulate the datasets list

  ###
  Listen to when a available datasets list has been populated and update
  the selected value on each model
  ###
  updateAvailableDatasets: ->
    selectedDatasets = @get "datasets"
    allSelected = _.isEmpty selectedDatasets
    @availableDatasets.forEach (dataset) -> 
      dataset.set 'selected',
          allSelected or _.contains( selectedDatasets, dataset.attributes.key),
          silent: true # don't notify that we have updated

  ###
  Listener for when the selected value of the available dataset has changed, 
  update the selected datasets accordingly. When all are selected the 
  the selected datasets array should be empty
  ###
  updateSelectedDatasets:->
    selectedDatasets = @availableDatasets
      .chain()
      .filter( (dataset) -> dataset.isSelected() )
      .map( (dataset) -> dataset.get "key" )
      .value() 

    allSelected = selectedDatasets.length is @availableDatasets.length
    @set "datasets", if allSelected then [] else selectedDatasets

  ###
  Get the list of datasets which contribute to this GridLayer.
  This method will perform a fetch on the availableDatasets and then
  return a jQuery promise of the Backbone.Collection
  ###
  fetchAvailableDatasets:->
    if @_availableDatasetsFetch? then return @_availableDatasetsFetch
    defer = $.Deferred()
    @availableDatasets.fetch reset:true, success: => defer.resolve(@availableDatasets)
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

  ###
  Returns the list of datasets which are currently contributing towards
  the layer
  ###
  getUsedDatasets: -> @availableDatasets.filter (dataset) -> dataset.isSelected() 