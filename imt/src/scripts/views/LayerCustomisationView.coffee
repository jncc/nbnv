define [
  "jquery"
  "backbone"
  "cs!views/ColourView"
  "cs!views/OpacityView"
  "cs!views/ResolutionView"
  "hbs!templates/LayerCustomisation"
], ($, Backbone, ColourView, OpacityView, ResolutionView, customisationTemplate) -> Backbone.View.extend
  ###
  The role of this view is to determine which sub customisation
  views are required for the given model that has been provided
  to this instance
  ###  
  initialize:->
    @$el.html customisationTemplate()

    @colorView = new ColourView
      model: @model
      el: @$('.colourPicker')

    @opacityView = new OpacityView
      model: @model
      el: @$('.opacity')

    @resolutionView = new ResolutionView
      model: @model
      el: @$('.resolution')

    @$el.dialog
      title: @model.getName()
      resizable: false
      width: 400
      close: -> $(this).dialog('destroy').remove() #remove the dialog from the dom on close

