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

  ###
  Generate the wms layer to request depending on the state of this GridLayer
  ###
  generateLayer: ->
    resolution = @get "resolution"
    layer = if @get "isPolygon" then "None-Grid" else 
            if resolution is 'auto' then "Grid-#{@get 'autoResolution'}"
            else "Grid-#{resolution}"
    @set 'layer', layer