define [
  'underscore'
  'backbone'
  "cs!helpers/Globals"
], (_, Backbone, Globals) -> Backbone.Model.extend 
  url: -> Globals.api "taxonObservations/#{@id}/types"

  ###
  Define the list of the different types of layer
  turn these on based upon the state of this model
  ###
  layerTypes: [ {relates: "hasGridPresence", isPresence: true, isPolygon : false}
                {relates: "hasGridAbsence", isPresence: false, isPolygon : false}
                {relates: "hasPolygonAbsence", isPresence: false, isPolygon: true}
                {relates: "hasPolygonPresence", isPresence: true, isPolygon: true}]
  
  ###
  Return an array of SingleSpeciesLayers which represent the different
  types of layers (other than the one represented by defaultLayer) which are 
  stored on the gateway for the given taxon.
  ###
  getOtherLayers: ->
    #Determine the type of the defaultLayer
    defaultLayer = @get('defaultLayer')
    flags = _.pick defaultLayer.attributes, 'isPolygon', 'isPresence'
    defaultLayerType = _.findWhere(@layerTypes, flags).relates
    
    _.chain(@layerTypes)
      .filter((curr) => @get curr.relates) #filter out the the map types which don't exist for this taxon
      .reject((curr) -> curr.relates is defaultLayerType)
      .map( (ele) -> defaultLayer.clone().set ele)
      .value()