define [
  "backbone",
  "cs!models/HabitatLayer",
  "cs!models/SiteBoundaryLayer",
  "cs!models/SingleSpeciesLayer"
], (Backbone, HabitatLayer, SiteBoundaryLayer, SingleSpeciesLayer) -> Backbone.Collection.extend
  model: (attr, options)->
    switch attr.entityType
      when "habitatdataset"       then return new HabitatLayer attr, options
      when "site boundarydataset" then return new SiteBoundaryLayer attr, options
      when "taxon"                then return new SingleSpeciesLayer attr, options

  ###
  Moves an existing element in the the collection from position index 
  to newPosition. Any "position" listeners of this instance will be 
  notified with the arguments: 
    model - the model which moved
    collection - this Layers instance
    newPosition - the new position of the model
    oldPosition - the position the model was in
  ###
  position: (index, newPosition) ->
    toMove = (@models.splice index, 1)[0]
    @models.splice newPosition, 0, toMove
    @trigger "position", toMove, @, newPosition, index
