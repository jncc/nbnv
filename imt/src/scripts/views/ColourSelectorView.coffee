define [
  "jquery"
  "underscore"
  "backbone"
  "hbs!templates/ColourSelectorView"
  "jquery.ui.slider"
], ($, _, Backbone, template) -> Backbone.View.extend
  ###
  Need to hard set the current slider value as the current one represents the old
  value 
  @see http://stackoverflow.com/questions/9121160/jquery-ui-slider-value-returned-from-slide-event-on-release-is-different-fro
  ###
  events:
    "slide .hue-slider" : (evt, ui) -> @hsl[0] = ui.value; do @updatePreview
    "slide .sat-slider" : (evt, ui) -> @hsl[1] = ui.value; do @updatePreview
    "slide .lum-slider" : (evt, ui) -> @hsl[2] = ui.value; do @updatePreview
    "slide .opacity-slider" : (evt, ui) -> @opacity = ui.value; do @updatePreview
    
  ###
  This view is resposible for controlling the colour attribute 
  of the given backbone model
  ###
  initialize:->    
    do @render

    @listenTo @model, 'change:colour', @updateColour
    @listenTo @model, 'change:opacity', @updateOpacity

  render: ->
    sliderOptions = max: 1, step: 0.01

    @$el.addClass "colourPicker"
    @$el.html template()

    #apply the same style as the layer to colour picker.
    #Background colour will be overridden
    @preview = @$('.preview').css @model.getLegendIcon()
    @hueSlider = @$('.hue-slider').slider sliderOptions
    @satSlider = @$('.sat-slider').slider sliderOptions
    @lumSlider = @$('.lum-slider').slider sliderOptions
    @opacitySlider = @$('.opacity-slider').slider sliderOptions

    @updateColour @model.get 'colour'
    @updateOpacity @model.getOpacity()
    do @updatePreview

  ###
  Get the current colour which this selector is offering
  ###
  getColour: -> @rgbToHex @hslToRgb @hsl


  ###
  Apply the current state of the view to the model
  ###
  apply:->
      @model.set "colour", @getColour()
      @model.set "opacity", @opacity

  ###
  Define a listener to be called when the opacity is updated
  ###
  updateOpacity: -> 
    @opacity = @model.getOpacity()
    @opacitySlider.slider "value", @opacity
    do @updatePreview

  ###
  Define a listener to be called when the colour is updated
  ###
  updateColour: ->
    @hsl = @rgbToHsl @hexToRgb @model.get 'colour'
    @hueSlider.slider "value", @hsl[0]
    @satSlider.slider "value", @hsl[1]
    @lumSlider.slider "value", @hsl[2]
    do @updatePreview

  ###
  Define the function to call to update the preview window and the sliders
  according the current state of @hsl and @opacity
  ###
  updatePreview: ->
    colourHex = "#" + @getColour()
     
    @satSlider.css "background-color": "#" + @rgbToHex @hslToRgb( [@hsl[0], 1,        0.5] )
    @lumSlider.css "background-color": "#" + @rgbToHex @hslToRgb( [@hsl[0], @hsl[1],  0.5] )
    @opacitySlider.css  "background-color": colourHex
    @preview.css        "background-color": colourHex, opacity: @opacity

  ###
  Translate the colour from rgb array to a hexadecimal value
  ###
  rgbToHex:(rgb) ->  
    _.chain(rgb)
      .map( (val) -> val.toString 16 )
      .map( (hex) -> if hex.length is 1 then return "0#{hex}" else return hex )
      .reduce( ((hex, comp) -> hex + comp), "")
      .value()
  
  ###
  Translate the given hex to an rgb array
  ###
  hexToRgb: (hex) ->[ parseInt(hex.slice(0,2), 16)
                      parseInt(hex.slice(2,4), 16)
                      parseInt(hex.slice(4,6), 16)]

  ###
  HSL to RGB function adapted from stackoverflow. Converted to CoffeeScript, and
  configured to take an array in the form of [h,s,l] rather than individual parameters.
  @see http://stackoverflow.com/questions/2353211/hsl-to-rgb-color-conversion
  ###
  hslToRgb: (hsl) ->
    [h,s,l] = hsl #split the array and assign to h, s, and l
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

  ###
  RGB to HSL function adapted from stackoverflow. Converted to CoffeeScript, and
  configured to take an array in the form of [r,g,b] rather than individual parameters.
  ###
  rgbToHsl: (rgb) ->
    r = rgb[0] / 255; g = rgb[1] / 255; b = rgb[2] / 255
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

    return [h,s,l]