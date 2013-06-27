define [
  "jquery"
  "underscore"
  "backbone"
  "hbs!templates/ResolutionSelector"
  "jquery-ui"
], ($, _, Backbone, template) -> Backbone.View.extend

  initialize:->
    do @render
    @$('input').click _.bind @handleButtonClick, @

    @listenTo @model, 'change:resolution', @updateChecked

  render:->
    @$el.html template @model
    @$el.addClass "resolution"
    @$el.buttonset()

  ###
  When the model changes, update the buttonset to reflect
  the currently clicked button
  ###
  updateChecked: ->
    res = @model.get 'resolution'
    @$("[id='resolution-#{res}']").click() #find the button to click on behalf of the user

  ###
  Handle the jquery click event when someone has requested 
  a different resolution
  ###
  handleButtonClick: (evt) ->
    resolutionClicked = $(evt.target).prop('id').split('-')[1]
    @model.set 'resolution', resolutionClicked