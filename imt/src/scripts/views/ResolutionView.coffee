define [
  "jquery"
  "underscore"
  "backbone"
  "jquery-ui"
], ($, _, Backbone) -> Backbone.View.extend

  initialize:->
    @$el.buttonset()
    @$('input').click _.bind @handleButtonClick, @

    @listenTo @model, 'change:resolution', @updateChecked

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