define [
  "backbone",
  "cs!models/HabitatLayer",
  "cs!models/SiteBoundaryLayer",
  "cs!models/SingleSpeciesLayer"
  "cs!models/DatasetSpeciesDensityLayer"
  "cs!models/DesignationSpeciesDensityLayer"
], (Backbone, HabitatLayer, SiteBoundaryLayer, SingleSpeciesLayer, DatasetSpeciesDensityLayer, DesignationSpeciesDensityLayer) -> Backbone.Collection.extend
  model: (attr, options)->
    switch attr.entityType
      when "habitatdataset"       then return new HabitatLayer attr, options
      when "site boundarydataset" then return new SiteBoundaryLayer attr, options
      when "taxon"                then return new SingleSpeciesLayer attr, options
      when "taxondataset"         then return new DatasetSpeciesDensityLayer attr, options
      when "designation"          then return new DesignationSpeciesDensityLayer attr, options

  ###
  Internal state, just a place to store the autoResolution 
  for this Layers collection
  ###
  state : new Backbone.Model
    autoResolution: "10km"
    maxResolution: "10km"

  initialize: ->
    #When a layer is added, synchronize that layer to this layers state
    @on "add", (layer) => @_syncLayer(layer)
    @listenTo @state, "change", => @forEach (layer) => @_syncLayer(layer)

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

  ###
  Lets mapping views notify when the zoom level has changed.
  The zoom level dictates which autoResolution and which resolutions
  are viewable in the map. This method will notify the relevant Layers
  of these changes and disable/reenable the Layers of this collection
  ###
  setZoom: (zoom) ->
    @state.set "autoResolution", 
      if 13 < zoom then '100m'
      else if 10 < zoom then '1km'
      else if 8 < zoom then '2km'
      else '10km'

    @state.set "maxResolution",
      if 11 < zoom then '100m'
      else if 7 < zoom then '1km'
      else if 6 < zoom then '2km'
      else if 3 < zoom then '10km'
      else 'Polygon'

  _syncLayer: (layer) -> layer.set @state.attributes if layer.isGridLayer

  ###
  Gets all the layers used datasets of this collection and return
  them merged together
  ###
  getUsedDatasets: ->
    @chain()
    .map( (layer) ->  layer.getUsedDatasets() )
    .flatten()
    .groupBy( (dataset) -> dataset.id )
    .map( (val, key) -> val[0])
    .value()

  ###
  Override the default get method for the collection. Backbone doesn't
  like it when collections contain two versions of a model with the same
  id.

  This get method reverts to using backbones client id, which is unique for
  all backbone models created
  ###
  get: (model)-> Backbone.Collection.prototype.get.call(this, model.cid)