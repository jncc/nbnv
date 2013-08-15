define [
  "jquery-md5"
  "underscore"
  "backbone"
], ($, _, Backbone, template) -> Backbone.View.extend
  initialize: ->
    @listenTo @model, 'change', @render

  render: () ->
    if @model.isLoggedIn()
      forename = @model.get "forename"
      surname = @model.get "surname"
      @$el.html "#{forname} #{surname}"
    else
      @$el.html """<a href="#{@model.getSSOHref()}">Login</a>"""