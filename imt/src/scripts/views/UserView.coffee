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
      @$el.html "#{forename} #{surname}"
    else
      @$el.empty()
          .append $('<a href="#">Login</a>').click =>
            window.location.href = @model.getSSOHref() #The sso href changes, get the latest