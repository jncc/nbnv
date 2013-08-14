define [
  "jquery"
  "underscore"
  "backbone"
  "hbs!templates/ColourSelectorView"
  "jquery-ui"
], ($, _, Backbone, template) -> Backbone.View.extend
  ###
  Need to hard set the current slider value as the current one represents the old
  value 
  @see http://stackoverflow.com/questions/9121160/jquery-ui-slider-value-returned-from-slide-event-on-release-is-different-fro
  ###
  events:
    "slide .hue-slider" : (evt, ui) -> @hueSlider.slider("value", ui.value); do @updatePreview
    "slide .sat-slider" : (evt, ui) -> @satSlider.slider("value", ui.value); do @updatePreview
    "slide .lum-slider" : (evt, ui) -> @lumSlider.slider("value", ui.value); do @updatePreview
    "slide .opacity-slider" : (evt, ui) -> @opacitySlider.slider("value", ui.value); do @updatePreview
    
  ###
  This view is resposible for controlling the colour attribute 
  of the given backbone model
  ###
  initialize:->    
    do @render

    @listenTo @model, 'change:colour', @updateColour
    @listenTo @model, 'change:opacity', @updateOpacity

  hueSlide:(evt, ui) -> updatePreview(ui.value,s,l,o)

  render: ->
    sliderOptions = max: 1, step: 0.01

    @$el.addClass "colourPicker"
    @$el.html template()

    @preview = @$('.preview')
    @hueSlider = @$('.hue-slider').slider sliderOptions
    @satSlider = @$('.sat-slider').slider sliderOptions
    @lumSlider = @$('.lum-slider').slider sliderOptions
    @opacitySlider = @$('.opacity-slider').slider sliderOptions

    @updateColour @model.get 'colour'
    @updateOpacity @model.getOpacity()
    do @updatePreview

  getColour:-> 
    rgb = @hslToRgb( @hueSlider.slider("value"),
                     @satSlider.slider("value"),
                     @lumSlider.slider("value"))

    _.chain(rgb)
      .map( (val) -> val.toString 16 )
      .map( (hex) -> if hex.length is 1 then return "0#{hex}" else return hex )
      .reduce( ((hex, comp) -> hex + comp), "")
      .value()

  getOpacity: -> @opacitySlider.slider("value") 

  updatePreview: (h,s,l,o) ->
    @preview.css
      "background-color": "#" + @getColour()
      "opacity": @getOpacity()

    hueRgb = @hslToRgb( @hueSlider.slider("value"), 1, 0.5)

    for i, slider of [@satSlider, @lumSlider, @opacitySlider]
      slider.css "background-color": "rgb(#{hueRgb[0]}, #{hueRgb[1]}, #{hueRgb[2]})"

  ###
  Define a listener to be called when the opacity is updated
  ###
  updateOpacity: -> 
    @opacitySlider.slider "value", @model.getOpacity()
    do @updatePreview

  ###
  Define a listener to be called when the colour is updated
  ###
  updateColour: ->
    hsl = @rgbToHsl(@hexToRgb( @model.get 'colour' ))
    @hueSlider.slider "value", hsl[0]
    @satSlider.slider "value", hsl[1]
    @lumSlider.slider "value", hsl[2]
    do @updatePreview

  hexToRgb: (hex) ->
    r: parseInt(hex.slice(0,2), 16)
    g: parseInt(hex.slice(2,4), 16)
    b: parseInt(hex.slice(4,6), 16)

  hslToRgb: (h,s,l) -> 
    if s is 0
      r = g = b = l #achromatic
    else
      hue2rgb = (p, q, t) ->
        if t < 0 then t += 1
        if t > 1 then t -= 1
        if t < 1/6 then return p + (q - p) * 6 * t
        if t < 1/2 then return q
        if t < 2/3 then return p + (q - p) * (2/3 - t) * 6
        return p
      
      q = if l < 0.5 then l * (1 + s) else l + s - l * s
      p = 2 * l - q;
      r = hue2rgb p, q, h + 1/3
      g = hue2rgb p, q, h
      b = hue2rgb p, q, h - 1/3

    return [Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)]

  rgbToHsl: (colour) ->
    r = colour.r / 255; g = colour.g / 255; b = colour.b / 255
    max = Math.max(r, g, b); min = Math.min(r, g, b)
    h = 0; s = 0; l = (max + min) / 2

    if max is min
      h = s = 0 #achromatic
    else
      d = max - min
      s = if l > 0.5 then d / (2 - max - min) else d / (max + min)
      switch max
          when r then h = (g - b) / d + (if g < b then 6 else 0)
          when g then h = (b - r) / d + 2
          when b then h = (r - g) / d + 4
      h /= 6

    return [h, s, l]
