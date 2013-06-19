define [
  'backbone', 
  'cs!collections/Search',
  'cs!collections/Layers'
], (Backbone, Search, Layers) -> Backbone.Model.extend 
  defaults :
    viewport: 
      minX:  -14.489099982674913
      maxX: 7.87906407581859
      minY: 49.825193671965025
      maxY: 59.45733404137668
    baseLayer: "Aerial"
    layers : new Layers()
    controlPanelVisible: false

  ###
  Return this models layers collection
  ###
  getLayers: -> @get "layers"

  ### 
  Return an instance of the NBN Gateways search api
  ###
  getSearch: -> new Search()

  getBaseLayers: -> ["OS", "Outline", "Shaded", "Aerial", "Hybrid"]

  ###
  Process a search result and update the application accordingly
  ###
  addSearchResult: (searchResult) ->
    @set "viewport", searchResult.worldBoundingBox if searchResult.worldBoundingBox
    @getLayers().add searchResult if not searchResult.worldBoundingBox