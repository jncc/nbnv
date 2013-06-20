define [
  "jquery"
  "backbone"
  "cs!views/ColourView"
  "hbs!templates/LayerCustomisation"
], ($, Backbone, ColourView, customisationTemplate) -> Backbone.View.extend
  ###
  The role of this view is to determine which sub customisation
  views are required for the given model that has been provided
  to this instance
  ###  
  initialize:->
    @$el.html customisationTemplate()

    @colorView = new ColourView
      model: @model
      el: $('.colourPicker', @$el)
