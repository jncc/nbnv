define [
  'underscore'
  'backbone' 
  'cs!collections/Search'
  'cs!collections/Layers'
], (_, Backbone, Search, Layers) -> Backbone.Model.extend 
  defaults :
    viewport: 
      minX:  -14.489099982674913
      maxX: 7.87906407581859
      minY: 49.825193671965025
      maxY: 59.45733404137668
    baseLayer: "Aerial"
    layers: new Layers
    controlPanelVisible: false

  initialize: ->
    #update the layers when zoom has changed
    @on "change:zoom", -> @getLayers().forEach (layer) => @_syncLayer(layer)
    @listenTo @getLayers(), "add", @_syncLayer #set the zoom when a layer is first added

  ###
  Return this models layers collection
  ###
  getLayers: -> @get "layers"

  getBaseLayers: -> ["OS", "Outline", "Shaded", "Aerial", "Hybrid"]

  ### 
  Return an instance of the NBN Gateways search api
  ###
  getSearch: -> new Search()

  ###
  Process a search result and update the application accordingly
  ###
  addSearchResult: (searchResult) ->
    @set "viewport", searchResult.worldBoundingBox if searchResult.worldBoundingBox
    @getLayers().add searchResult if not searchResult.worldBoundingBox

  ###
  Synchronize the given layer to this application.
  ###
  _syncLayer: (layer) -> 
    layer.set 'autoResolution', @getMostAppropriateLayerForZoom(@get 'zoom') if layer.isGridLayer

  getMostAppropriateLayerForZoom: (zoom)->
    zoom = @get 'zoom'
    if 13 < zoom then '100m'
    else if 10 < zoom then '1km'
    else if 8 < zoom then '2km'
    else '10km'