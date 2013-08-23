define [
  'underscore'
], (_) ->

  ###
  A viewport consistes of a 4 part comma seperated string in the form
  minX,minY,maxX,maxY

  This function will split the array and return an object representation
  ###
  expandViewport: (minViewport) ->
    [minX,minY,maxX,maxY] = _.map minViewport.split(','), parseFloat
    minX:minX, minY:minY, maxX:maxX, maxY:maxY
  
  ###
  Given a viewport, use a max of 3 decimal places for each part then
  join in a comma sepearated string
  ###
  shrinkViewport: (viewport) ->
    viewportArr = [viewport.minX, viewport.minY, viewport.maxX, viewport.maxY]
    _.map(viewportArr, (num) -> num.toFixed(3)).join ','