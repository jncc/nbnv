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
    @on 'change:resolution change:isPolygon change:autoResolution', -> @trigger 'change:name'

  ###
  Works out what the current resolution is, if this is a Polygon layer
  returns "Polygon"
  ###
  getCurrentResolution: ->
    resolution = @get "resolution"
    if @get "isPolygon" then "Polygon" else 
    if resolution is 'auto' then @get 'autoResolution'
    else resolution


  ###
  Generate the label for the grid layer
  ###
  getName: ->
    "#{@getCurrentResolution()} #{@mapOf()} for #{@attributes.name}"

  ###
  Generate the wms layer to request depending on the state of this GridLayer
  ###
  generateLayer: ->
    resolution = @getCurrentResolution()
    @set 'layer', if resolution is "Polygon" then "None-Grid" else "Grid-#{resolution}"