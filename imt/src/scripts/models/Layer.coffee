define [
  "backbone"
], (Backbone) -> Backbone.Model.extend
  ###
  Gets any sld styling info which should be sent with each wms request
  ###
  getSLD:-> @get "sld"

  ###
  Returns the name of the layer as should be displayed in the legend
  ###
  getName:-> @get "name"

  ###
  Returns the address to the wms end point which should be used when
  making wms requests
  ###
  getWMS:-> @get "wms"

  ###
  Gets the name of the wms layer which should be requested on each
  wms request
  ###
  getLayer:-> @get "layer"

  ###
  Get the current opacity value between 0-1 for this layer
  ###
  getOpacity: -> @get "opacity"

  ###
  Check if this layer is visible and should be renedered
  ###
  isVisible: -> @get "visibility"

  ###
  Returns a list of Dataset models for each dataset which is used to
  produce the layer
  ###
  getUsedDatasets: -> []

  ###
  Returns a css styling object for styling the legend icon which should
  represent this layer. The styling should cover 20px x 20px
  ###
  getLegendIcon: -> {}