define [
  "jquery-md5"
  "underscore"
  "backbone"
], ($, _, Backbone, template) -> Backbone.View.extend
  initialize: ->
    @listenTo @model, 'change', @render

  render: () ->
    if @model.isLoggedIn()
      @$el.html @model.get "email"
    else
      @$el.html """<a href="#{@model.getSSOHref()}">Login</a>"""