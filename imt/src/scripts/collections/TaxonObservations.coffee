define [
  'underscore'
  'backbone'
  'cs!helpers/Globals' 
], (_, Backbone, Globals) -> Backbone.Collection.extend 

  url: Globals.api "taxonObservations"

  initialize: (models, options) ->
    @ptvk = options.ptvk
    @startYear = options.startYear
    @endYear = options.endYear
    @datasetKey = options.datasetKey

  ###
  Overide the default fetch method so that it supports a
  polygon option which is the wkt of the area to fetch for.
  ###
  fetch: (options) ->
    #Modifiy the options.data object
    options.data =
      polygon: options.polygon
      ptvk: @ptvk
      startYear: @startYear
      endYear: @endYear
      datasetKey: @datasetKey

    Backbone.Collection.prototype.fetch.call this, options