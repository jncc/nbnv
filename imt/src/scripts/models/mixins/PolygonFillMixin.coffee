define [
  "jquery-md5"
  "hbs!templates/slds/Default"
  "hbs!templates/slds/Hatching"
], ($, polygonFill, polygonHatching) ->
  isStyleable: true #expose that this layer is styleable

  initialize: () ->
    @set "colour", $.md5(@id).substring 0, 6 if not @has 'colour'

    @on 'change:colour change:symbol', -> @trigger 'change:legendIcon'
    @on 'change:colour change:symbol change:layer', -> @trigger 'change:sld'

  ###
  The following method will work out the sld template which
  should be used depending on the current symbol. Once determined
  it will be called with the current layer and selected colour
  ###
  getSLD: -> 
    sldTemplate = switch @get "symbol" 
      when "fill" then polygonFill
      when "hatching" then polygonHatching

    sldTemplate
      layer: @getLayer(),
      colour: @get "colour"

  ###
  Generate the current legend icon given the symbol and colour
  ###
  getLegendIcon: ->
    switch @get "symbol"
      when 'fill'
        backgroundColor: "#" + @get "colour"
        backgroundImage: ""
      when 'hatching'
        backgroundColor: "#" + @get "colour"
        backgroundImage: "url('img/polygonHatching.png')"