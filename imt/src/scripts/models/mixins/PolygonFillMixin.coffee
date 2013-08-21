define [
  "jquery-md5"
  "hbs!templates/slds/Default"
  "hbs!templates/slds/Hatching"
], ($, polygonFill, polygonHatching) ->
  isStyleable: true #expose that this layer is styleable

  initialize: ->
    @set "colour", @getDefaultColour() if not @has 'colour'

    @on 'change:colour change:symbol', -> @trigger 'change:legendIcon'
    @on 'change:colour change:symbol change:layer', -> @trigger 'change:sld'


  ###
  Generates a default colour for this layer based on the md5 hash 
  of the layers id
  ###
  getDefaultColour: -> $.md5(@id).substring 0, 6

  ###
  Checks if the current colour is different to the default colour
  ###
  isDefaultAppearance: -> @getDefaultColour() is @get("colour") and @get("opacity") is @defaults.opacity

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