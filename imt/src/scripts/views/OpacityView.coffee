define [
  "jquery"
  "backbone"
  "jquery-ui"
], ($, Backbone) -> Backbone.View.extend
  ###
  This view is resposible for controlling the opacity attribute 
  of the given backbone model
  ###
  initialize:->
    @$el.addClass "opacity"
    @$el.slider
      value: @model.getOpacity() * 100
      slide: (event, ui) => @model.set "opacity", ui.value/100