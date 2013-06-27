define [
  "jquery"
  "underscore"
  "backbone"
  "hbs!templates/DatasetSelector"
  "jquery-ui"
], ($, _, Backbone, template) -> Backbone.View.extend

  initialize:->
    @listenTo @collection, 'reset', @render #listen to when the collection is loaded
    @listenTo @collection, 'change:selected', @updateCheckbox #listen to when one checkbox has changed
    @$el.on "click", "input", _.bind @handleClick, @ #add event listener to when checkboxs are clicked

    do @render #render for the first time

  render:->
    @$el.html template @collection.toJSON()

  handleClick: (evt) ->
    checkbox = $(evt.target)
    datasetKey = checkbox.attr "value"
    @collection.get(datasetKey).set "selected", checkbox.is(':checked')

  ###
  Get the checkbox which relates to the updated model and
  update its checked property
  ###
  updateCheckbox: (model) ->
    @$("input[value='#{model.id}'']").prop "checked", model.get "selected"