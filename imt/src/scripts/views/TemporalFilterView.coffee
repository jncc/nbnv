define [
  "jquery"
  "underscore"
  "backbone"
  "hbs!templates/TemporalFilter"
  "jquery-ui"
], ($, _, Backbone, template) -> Backbone.View.extend

  initialize:->
    do @render  

    @listenTo @model, "change:startDate change:endDate", @updateView
    @listenTo @model, "invalid", @handleValidation

    @$('.startDate').change (evt) => do @removeErrorMessage
    
    @$('.endDate').change (evt) => do @removeErrorMessage
     

  ###
  Apply the current state of the view to the model
  ###
  apply: ->
    @model.set "startDate", parseInt( @$('.startDate').val(), 10 ), validate: true
    @model.set "endDate", parseInt( @$('.endDate').val(), 10 ), validate: true
    
  ###
  Set up the html content for this view
  ###
  render:->
    @$el.addClass "temporal"
    @$el.html template @model #set the html content

  ###
  Update the view with data from the model
  ###
  updateView:->
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