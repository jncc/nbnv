define [
  'cs!helpers/Numbers'
], (Numbers) ->
  ###
  The earliest year which can be recorded on the nbn gateway is 1600.
  If we subtract this value from the start year and end year we can represent
  a year range of 1600 - 2112 in 18bits (3 Base64 digits)
  ###
  shrinkYearRange: (yearRange) ->
    miniYearRange = (yearRange.startDate - 1600) * 512 + yearRange.endDate - 1600
    Numbers.toBase64 miniYearRange

  expandYearRange: (miniYearRange) ->
    miniYearRange = Numbers.fromBase64 miniYearRange
    startDate: 1600 + (Math.floor miniYearRange / 512) # shift by 9 bits
    endDate:   1600 + (miniYearRange & 0x1FF)          # mask with last 9 bits