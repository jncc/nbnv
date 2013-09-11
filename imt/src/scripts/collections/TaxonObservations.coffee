define [
  'underscore'
  'backbone'
  'cs!helpers/Globals' 
], (_, Backbone, Globals) -> Backbone.Collection.extend 

  url: Globals.api "taxonObservations"

  initialize: (models, options) ->
    @ptvk = options.ptvk
    @designation = options.designation
    @startYear = options.startYear
    @endYear = options.endYear
    @datasetKey = options.datasetKey
    @layer = options.layer

  ###
  Overide the default fetch method so that it supports a
  polygon option which is the wkt of the area to fetch for.
  ###
  fetch: (options) ->
    #console.log @layer.availableDatasets
    #Modifiy the options.data object
    options.data =
      polygon: options.polygon
      designation: @designation
      ptvk: @ptvk
      startYear: @startYear
      endYear: @endYear
      datasetKey: @datasetKey

    options.traditional = true

    Backbone.Collection.prototype.fetch.call this, options