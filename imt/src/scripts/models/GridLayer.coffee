define [
  "underscore"
  "cs!models/Layer"
], (_, Layer) -> Layer.extend
  ###
  Flag that this is a grid layer and should be updated by the App
  when the auto resolution changes
  ###
  isGridLayer: true
  
  initialize: () ->
    @on 'change:resolution change:isPolygon change:autoResolution', -> do @generateLayer
    @on 'change:resolution change:isPolygon change:autoResolution change:startDate change:endDate', -> @trigger 'change:name'
    @on 'change:resolution change:isPolygon change:autoResolution change:maxResolution', -> @trigger 'change:visibility'

  ###
  Works out what the current resolution is, if this is a Polygon layer
  returns "Polygon"
  ###
  getCurrentResolution: ->
    resolution = @get "resolution"
    if @get "isPolygon" then "Polygon" 
    else if resolution is 'auto' then @get 'autoResolution'
    else resolution

  ###
  Generate the label for the grid layer
  ###
  getName: ->
    "#{@attributes.name} #{@getCurrentResolution()} #{@mapOf()}"

  ###
  Determines if the given layer is currently in range and can be rendered
  on the map
  ###
  isInRange: ->
    resolutionOrders = ["Polygon", "10km", "2km", "1km", "100m"]
    layerPos = _.indexOf resolutionOrders, @getCurrentResolution()
    allowedPos = _.indexOf resolutionOrders, @get "maxResolution"
    layerPos <= allowedPos

  ###
  Generate the wms layer to request depending on the state of this GridLayer
  ###
  generateLayer: ->
    resolution = @getCurrentResolution()
    @set 'layer', if resolution is "Polygon" then "None-Grid" else "Grid-#{resolution}"

  ###
  Check if this layer is visible and should be renedered
  ###
  isVisible: -> Layer.prototype.isVisible.call(this, arguments) and @isInRange()