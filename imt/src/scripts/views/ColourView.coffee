define [
  "jquery"
  "backbone"
  "jquery-colorpicker"
], ($, Backbone) -> Backbone.View.extend
  ###
  This view is resposible for controlling the colour attribute 
  of the given backbone model
  ###
  initialize:->
    @$el.ColorPicker 
      flat: true
      onSubmit: (hsb, hex, rgb) => @model.set 'colour', hex
