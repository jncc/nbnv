define [
  'underscore'
  'cs!helpers/Numbers'
], (_, Numbers) ->
  BASE_LAYERS = ["OS", "Outline", "Shaded", "Aerial", "Hybrid"]

  ###
  Given the single character which represents the current base layer
  convert to a number, look that number up in the BASE_LAYERS and return
  the real name
  ###
  expandBaseLayer: (minBaseLayer) -> BASE_LAYERS[Numbers.fromBase64 minBaseLayer]
  
  ###
  Given a baselayer name, look it up in the array, and then return the id
  of that layer as a base64 encoded character
  ###
  shrinkBaseLayer: (baseLayer) -> Numbers.toBase64 _.indexOf BASE_LAYERS, baseLayer