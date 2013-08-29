define [
  "jquery"
  "underscore"
  "backbone"
  "hbs!templates/DatasetSelector"
  "jquery-ui"
], ($, _, Backbone, template) -> Backbone.View.extend
  events:
    "click .toggle-container":  "updateToggle"

  initialize:->
    @listenTo @collection, 'reset', @render #listen to when the collection is loaded
    @listenTo @collection, 'change:selected', @updateCheckbox #listen to when one checkbox has changed
    
    do @render #render for the first time

  ###
  Apply the current state of the view to the model
  ###
  apply :->
    @$('.datasets-container input').each (i, ele)=>
      checkbox = $(ele)
      datasetKey = checkbox.attr "value"
      @collection.get(datasetKey).set "selected", checkbox.is(':checked')

  render:->
    @$el.addClass "datasets"
    @$el.html template @collection.toJSON()

    allSelected = @$('.datasets-container input[type=checkbox]:not(:checked)').length is 0
    @$(".toggle-container input").prop "checked", allSelected

  updateToggle: ->
    toggle = @$(".toggle-container input").is ':checked'
    @$(".datasets-container input").prop "checked", toggle

  ###
  Get the checkbox which relates to the updated model and
  update its checked property
  ###
  updateCheckbox: (model) ->
    @$(".datasets-container input[value='#{model.id}']").prop "checked", model.get "selected"