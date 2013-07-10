define [
  'underscore'
  'backbone'
  'cs!collections/Layers'
  'cs!models/User'
], (_, Backbone, Layers, User) -> Backbone.Model.extend 
  defaults :
    viewport: 
      minX:  -14.489099982674913
      maxX: 7.87906407581859
      minY: 49.825193671965025
      maxY: 59.45733404137668
    baseLayer: "Aerial"
    layers: new Layers
    controlPanelVisible: false

  user: new User

  initialize: () ->
    do @user.fetch


  getUser: -> @user

  ###
  Return this models layers collection
  ###
  getLayers: -> @get "layers"

  getBaseLayers: -> ["OS", "Outline", "Shaded", "Aerial", "Hybrid"]

  ###
  Process a search result and update the application accordingly
  ###
  addSearchResult: (searchResult) ->
    @set "viewport", searchResult.worldBoundingBox if searchResult.worldBoundingBox
    
    if not searchResult.worldBoundingBox
      @getLayers().add searchResult
      @set 'controlPanelVisible', true