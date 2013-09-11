define [
  'underscore'
  'backbone'
  'cs!collections/Layers'
  'cs!models/User'
  'cs!models/Picker'
], (_, Backbone, Layers, User, Picker) -> Backbone.Model.extend 
  defaults :
    viewport: 
      minX: -14.489099982674913
      maxX: 7.87906407581859
      minY: 49.825193671965025
      maxY: 59.45733404137668
    baseLayer: "Aerial"
    layers: new Layers
    currentUser: new User
    controlPanelVisible: false
    loading: false

  _loadingCount: 0

  initialize: () ->
    do @getCurrentUser().fetch
    @set "picker", new Picker layers: @getLayers()

  ###
  Returns the user whose access is currently being used
  ###
  getCurrentUser: -> @get "currentUser"

  ###
  Return this models layers collection
  ###
  getLayers: -> @get "layers"

  getPicker: -> @get "picker"

  getBaseLayers: -> ["OS", "Outline", "Shaded", "Aerial", "Hybrid"]

  ###
  Process a search result and update the application accordingly
  ###
  addSearchResult: (searchResult) ->
    @set "viewport", searchResult.worldBoundingBox if searchResult.worldBoundingBox
    
    if not searchResult.worldBoundingBox
      @getLayers().add searchResult, addOtherTypes: true
      @set 'controlPanelVisible', true

  ###
  Various parts of the application can call this method to notfy that a loading event
  has started. On the condition that the same code will call decreaseLoadingCount
  when that loading event has completed
  ###
  increaseLoadingCount: -> 
    @_loadingCount++
    @set 'loading', true

  ###
  To be called after an increaseLoadingCount has been called
  ###
  decreaseLoadingCount: -> 
    if --@_loadingCount is 0
      @set 'loading', false