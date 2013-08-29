define [
  'cs!helpers/Numbers'
], (Numbers) ->
  ###
  The style component is represented as a 30 bit number, this is so that it always neatly
  fits into 5 rixits. 8 bits for the r,g,b values and 6 for the opacity level
  ###
  expandStyle: (minStyle) ->
    minValue = Numbers.fromBase64 minStyle
    colour = Math.floor(minValue / 64).toString 16 #Shift 6 bits and hex
    
    opacity : (minValue & 0x3F) / 63 #mask with last 6 digits, convert to [0..1]
    colour : Numbers.pad colour, 6
  
  ###
  Convert the style to a base64 encoded string
  ###
  shrinkStyle: (style) ->
    rgb = parseInt style.colour, 16
    opacity = Math.floor(style.opacity * 63)
    Numbers.toBase64 rgb * 64 + opacity