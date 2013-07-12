define [
  'jquery'
  'underscore'
  'backbone' 
  'cs!collections/Layers'
  'cs!collections/Features'
  'cs!models/Feature'
], ($, _, Backbone, Layers, Features, Feature) -> Backbone.Model.extend 
  defaults :
    features: new Features
    isPicking: false
    layers: new Layers
    wkt: ""

  _deferred: $.Deferred()

  initialize: -> 
    @on 'change:wkt', @updateFeatures

  getLayers: -> @get "layers"

  getFeatures: -> @get "features"

  ###
  The following method will search through the layers and check to see
  if they have a getTaxonObservations method. If they do, it will be called
  and the returned collection will be fetched with the current wkt.

  This method returns a jquery promise for when the request is completed
  ###
  updateFeatures: ->
    do @_deferred.reject #reject the last request
    @_deferred = $.Deferred() #and create a new one

    features = @getFeatures()
    do features.reset #remove all of the features currently in the collection

    #Object an array of collections which i can query
    collectionsToFetch = @getLayers()
      .chain()
      .filter( (layer) -> layer.getTaxonObservations? )
      .map( (layer) -> layer.getTaxonObservations() )
      .value()

    #Fetch all the layers and store the promises in an array
    fetchPromises = collectionsToFetch.map (layer) => layer.fetch 
      polygon: @get 'wkt'

    #Bundle all the promises together and return a single promise for when all
    #layers have been 'picked'
    $.when.apply($, fetchPromises)
      .done => @_deferred.resolve @getFeatures()

    #When done, add all the obtained observations to the features collection
    @_deferred
      .done(-> _.each collectionsToFetch, (layer) -> features.add layer)
      .promise()