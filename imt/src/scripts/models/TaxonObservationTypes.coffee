define [
  'underscore'
  'backbone'
  "cs!helpers/Globals"
], (_, Backbone, Globals) -> Backbone.Model.extend 
  url: -> Globals.api "taxonObservations/#{@ptvk}/types"

  ###
  Define the list of the different types of layer
  turn these on based upon the state of this model
  ###
  layerTypes: [ {relates: "hasGridPresence", isPresence: true, isPolygon : false}
                {relates: "hasGridAbsence", isPresence: false, isPolygon : false}
                {relates: "hasPolygonAbsence", isPresence: false, isPolygon: true}
                {relates: "hasPolygonPresence", isPresence: true, isPolygon: true}]

  initialize: (attr, options) ->
    @ptvk = options.ptvk
    @instance = options.instance
  
  ###
  Return an array of SingleSpeciesLayers which represent the different
  types of layers (other than the one represented by instance) which are 
  stored on the gateway for the given taxon.
  ###
  getOtherLayers: ->
    #Determine the type of the instance
    flags = _.pick @instance.attributes, 'isPolygon', 'isPresence'
    instanceType = _.findWhere(@layerTypes, flags).relates

    _.chain(@layerTypes)
      .filter((curr) => @get curr.relates) #filter out the the map types which don't exist for this taxon
      .reject((curr) -> curr.relates is instanceType)
      .map( (ele)=> @instance.clone().set ele)
      .value()