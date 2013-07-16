define [
  'jquery'
  'underscore'
  'backbone' 
  'cs!collections/Layers'
], ($, _, Backbone, Layers) -> Backbone.Model.extend 
  defaults :
    resultsForLayers: []
    isPicking: false
    layers: new Layers
    wkt: ""

  initialize: -> 
    @on 'change:wkt', @updateResultsForLayers

  getLayers: -> @get "layers"

  getResultsForLayers: -> @get "resultsForLayers"

  ###
  The following method will search through the layers and check to see
  if they have a getTaxonObservations method. If they do, a new model will 
  be added to the resultsForLayers collection and which represents the result of
  getTaxonObservations and the layer the observations are linked to.

  The TaxonObservations models will then be fetched for the currently selected polygon
  ###
  updateResultsForLayers: ->
    #Object an array of pickerResultsForLayers which i can query
    resultsForLayers = @getLayers()
      .chain()
      .filter( (layer) -> layer.getTaxonObservations? )
      .map( (layer) -> layer: layer, records: layer.getTaxonObservations() )
      .value()

    #Fetch all the layers and store the promises in an array
    _.each resultsForLayers, (resultsForLayer) => resultsForLayer.records.fetch
      polygon: @get "wkt"

    @set "resultsForLayers", resultsForLayers