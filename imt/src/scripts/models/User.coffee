define [
  "jquery"
  "underscore"
  "backbone"
  "cs!helpers/Globals"
], ($, _, Backbone, Globals) -> Backbone.Model.extend
  defaults:
    id: 1

  ###
  The currently logged in user lives at api/user
  ###
  url: -> Globals.api "user"

  ###
  Return the link which should visited in order
  to login to the NBN Gateway and return to the IMT
  ###
  getSSOHref:-> Globals.portal "User/SSO/Login?redirect=#{document.URL}"

  ###
  Determine if the current user is logged in
  ###
  isLoggedIn :-> @id isnt 1