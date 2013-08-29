define [
  'cs!helpers/Numbers'
], (Numbers) ->
  RESOLUTIONS = ['10km', '2km', '1km', '100m', 'auto']

  ###
  The selected resolution of a map can either be one of the following: 
  * 10km - 0
  * 2km - 1
  * 1km - 2
  * 100m - 3
  * auto - 4
  * Polygon (inferred from other values) - 5

  The 6 options can be represented as a 3 bit number, however each has an absence flag.
  Therefore the layers to be requested can be represented as a 4 bit number (or one Base64 digit)
  ###
  shrinkResolution: (layer) ->
    resolution = if layer.isPolygon then 5 else _.indexOf RESOLUTIONS, layer.resolution
    Numbers.toBase64 resolution * 2 + (1 & layer.isPresence) #shift 1 bit for the presence/absence flag

  expandResolution: (miniResolution) ->
    modifiers = Numbers.fromBase64 miniResolution
    resolution = Math.floor modifiers / 2
    
    isPolygon: resolution is 5
    isPresence: (modifiers & 0x1) is 1 #mask to get the presence absence flag, turn to boolean
    resolution: RESOLUTIONS[resolution] if resolution isnt 5