define [
  "cs!models/Layer"
], (Layer) -> Layer.extend
  ###
  Flag that this is a grid layer and should be updated by the App
  when the auto resolution changes
  ###
  isGridLayer: true
  
  initialize: () ->
    @on 'change:resolution change:isPolygon change:autoResolution', => do @refreshLayer

  refreshLayer: ->
    resolution = @get "resolution"
    layer = if @get "isPolygon" then "None-Grid" else 
            if resolution is 'auto' then "Grid-#{@get 'autoResolution'}"
            else "Grid-#{resolution}"
    @set 'layer', layer