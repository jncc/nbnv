define [
  "backbone"
  "cs!models/mixins/DatasetFilterMixin"
], (Backbone, DatasetFilterMixin) ->
  describe "DatasetFilterMixin Tests", ->  
    datasetFilterMixin = null

    ###

    The dataset mixin needs to be mixed in with a model which supports
    getAvailableDatasets()

    Mixining in with the default Backbone model means that it can not
    be instantiated 
    beforeEach ->
      DatasetFilterModel = Backbone.Model.extend DatasetFilterMixin
      datasetFilterMixin = new DatasetFilterModel
    

    it "Should be filterable by default", ->
      expect(datasetFilterMixin.isDatasetFilterable()).toBeTruthy
    ###
#
#  ###
#  Listen to when a available datasets list has been populated and update
#  the selected value on each model
#  ###
#  updateAvailableDatasets: ->
#    selectedDatasets = @get "datasets"
#    allSelected = _.isEmpty selectedDatasets
#    @availableDatasets.forEach (dataset) -> 
#      dataset.set 'selected',
#          allSelected or _.contains( selectedDatasets, dataset.attributes.key),
#          silent: true # don't notify that we have updated
#
#  ###
#  Listener for when the selected value of the available dataset has changed, 
#  update the selected datasets accordingly. When all are selected the 
#  the selected datasets array should be empty
#  ###
#  updateSelectedDatasets:->
#    selectedDatasets = @availableDatasets
#      .chain()
#      .filter( (dataset) -> dataset.isSelected() )
#      .map( (dataset) -> dataset.get "key" )
#      .value() 
#
#    allSelected = selectedDatasets.length is @availableDatasets.length
#    @set "datasets", if allSelected then [] else selectedDatasets
#
#  ###
#  Get the list of datasets which contribute to this GridLayer.
#  This method will perform a fetch on the availableDatasets and then
#  return a jQuery promise of the Backbone.Collection
#  ###
#  fetchAvailableDatasets:->
#    if @_availableDatasetsFetch? then return @_availableDatasetsFetch
#    defer = $.Deferred()
#    @availableDatasets.fetch reset:true, success: => defer.resolve(@availableDatasets)
#    @_availableDatasetsFetch = defer.promise()
#
#  ###
#  Returns the list of datasets which are currently contributing towards
#  the layer
#  ###
#  getUsedDatasets: -> @availableDatasets.filter (dataset) -> dataset.isSelected() 