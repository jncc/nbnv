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
    #use the given datasets as a lookup to obtain the orgainsation name
    @availableDatasets = options.availableDatasets 
    @absence = options.absence

  ###
  Check to see if this collection is populated based on a restriction to a 
  single taxon
  ###
  isFilteredByTaxon: -> @ptvk?

  ###
  Overide the default fetch method so that it supports a
  polygon option which is the wkt of the area to fetch for.
  ###
  fetch: (options) ->
    #Modifiy the options.data object
    options.data =
      polygon: options.polygon
      designation: @designation
      ptvk: @ptvk
      startYear: @startYear
      endYear: @endYear
      datasetKey: @datasetKey
      absence: @absence

    options.traditional = true

    Backbone.Collection.prototype.fetch.call this, options

  ###
  By using the supplied @availabelDatasets we can make the toJSON response of 
  this collection more complete. Attach the full dataset definition to the observation
  ###
  toJSON: ->
    observations = Backbone.Collection.prototype.toJSON.apply this, arguments
    if observations.length == 1 && observations[0].hasOwnProperty("success") && observations[0].success == false
      window.apiFailed = true
      window.apiFailureMessage = observations[0].status
      window.apiFailureMessageDisplayed = false   
    else
      _.each(observations, (observation) => observation.dataset = @availableDatasets
                                                                    .get(observation.datasetKey)
                                                                    .toJSON() )
      return observations
    return []