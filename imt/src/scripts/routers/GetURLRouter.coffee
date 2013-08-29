define [
  "jquery"
  "underscore"
  "cs!models/HabitatLayer"
  "cs!models/SiteBoundaryLayer"
  "cs!models/SingleSpeciesLayer"
  "cs!models/DatasetSpeciesDensityLayer"
  "cs!models/DesignationSpeciesDensityLayer"
], ($, _, HabitatLayer, SiteBoundaryLayer, SingleSpeciesLayer, DatasetSpeciesDensityLayer, DesignationSpeciesDensityLayer) -> class
  constructor: (options) ->
    @model = options.model; #store a reference to the passed in model

  navigate: (queryString) ->
    query = @createQueryObject(queryString)

    @model.set "baseLayer", query.baselayer if query.baselayer?
    @model.set "viewport", @getBBox(query.bbox), showAll: true if query.bbox? 
    @getLayers @model.getLayers(), query

  getLayers: (layers, query)->
    layersToFetch = []

    if query.habitats? 
      _.each query.habitats.split(','), (key)-> layersToFetch.push( new HabitatLayer key: key )

    if query.boundary? 
      _.each query.boundary.split(','), (key)-> layersToFetch.push( new SiteBoundaryLayer key: key )

    switch query.mode
      when "SPECIES"        then layersToFetch.push @singleSpeciesLayer(query)
      when "SINGLE_DATASET" then layersToFetch.push @datasetLayer(query)
      when "DESIGNATION"    then layersToFetch.push @designationLayer(query)

    # TODO: Here we load up the layers and add them according to the order in which 
    # ajax requests return. This should really be chained so that the layersToFetch
    # order is maintained
    _.each layersToFetch, (layer) -> 
      promise = do layer.fetch
      promise.done -> layers.add layer, addOtherTypes: true


  singleSpeciesLayer: (query) -> new SingleSpeciesLayer 
    ptaxonVersionKey: query.species
    datasets: query.datasets?.split ','
    startDate: query.startyear
    endDate: query.endyear

  datasetLayer: (query) -> new DatasetSpeciesDensityLayer
    key: query.dataset
    startDate: query.startyear
    endDate: query.endyear

  designationLayer: (query) -> new DesignationSpeciesDensityLayer
    code: query.designation
    datasets: query.datasets?.split ','
    startDate: query.startyear
    endDate: query.endyear

  createQueryObject: (queryString) ->
    queryParts = queryString.substring(1).split '&'
    _(queryParts)
      .chain()
      .map( (queryPart) -> queryPart.split '=' )
      .reduce( (obj, parts) -> 
          obj[parts[0]] = parts[1]
          return obj
        , {})
      .value()

  getBBox: (bboxStr) ->
    bboxParts = bboxStr.split ','
    minX: bboxParts[0]
    minY: bboxParts[1]
    maxX: bboxParts[2]
    maxY: bboxParts[3]