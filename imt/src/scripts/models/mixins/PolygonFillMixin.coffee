define [
  "jquery-md5"
  "hbs!templates/slds/Default"
], ($, sld) ->
  initialize: () ->
    console.log @id
    @set "colour", $.md5(@id).substring 0, 6

    @on 'change:colour', -> @trigger 'change:legendIcon'
    @on 'change:colour change:layer', -> @trigger 'change:sld'

  getSLD: -> sld
    layer: @getLayer(),
    colour: @get "colour"

  getLegendIcon: ->
    backgroundColor: "#" + @get "colour"