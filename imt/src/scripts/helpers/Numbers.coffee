define [], () ->
  ###
  Number to base64 implementation taken from here stackoverflow
  http://stackoverflow.com/questions/6213227/fastest-way-to-convert-a-number-to-radix-64-in-javascript
  ###
  _rixits : "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz+/"
  
  toBase64 : (number) -> 
    throw "The input is not valid" if isNaN Number number or 
                                      not number? or 
                                      number is Number.POSITIVE_INFINITY
    throw "Can't represent negative numbers now" if number < 0
    residual = Math.floor number
    result = ''
    while true
      rixit = residual % 64
      result = @_rixits.charAt(rixit) + result
      residual = Math.floor residual / 64
      break if residual is 0
    return result

  fromBase64 : (rixits) ->
    result = 0
    rixits = rixits.split ''
    for e in rixits
      result = (result * 64) + @_rixits.indexOf e
    return result

  ###
  A simple function to add leading zeros to a String representation of a Number
  ###
  pad : (num, length) ->
    str = num.toString()
    while str.length < length
      str = '0' + str
    return str