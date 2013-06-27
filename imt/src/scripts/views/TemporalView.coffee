define [
  "jquery"
  "underscore"
  "backbone"
  "jquery-ui"
], ($, _, Backbone) -> Backbone.View.extend

  initialize:->
    @listenTo @model, "change:startDate change:endDate", @render
    @listenTo @model, "invalid", @handleValidation

    @$('.startDate').change (evt) => 
      do @removeErrorMessage
      @model.set "startDate", parseInt( $(evt.target).val() ), validate: true
 
    @$('.endDate').change (evt) =>
      do @removeErrorMessage
      @model.set "endDate", parseInt( $(evt.target).val() ), validate: true

  ###
  Update the view with data from the model
  ###
  render:->
    @$('.startDate').html @model.get "startDate"
    @$('.endDate').html @model.get "endDate"

  ###
  Go through the errors array and look for objects whose name is temporalFilter
  @see models/mixins/TemporalFilterMixin
  ###
  handleValidation: (model, errors) ->
    @$('.control-group').addClass('error')
    @$('.help-inline').html _.chain(errors)
                              .filter( (ele) -> ele.name is "temporalFilter")
                              .map( (ele) -> ele.message )
                              .value()
                              .join('<br>')

  ###
  Clears the error class and any error message
  ###
  removeErrorMessage:->
    @$('.control-group').removeClass('error')
    @$('.help-inline').html ""