define [
  "jquery"
  "backbone"
], ($, Backbone) -> Backbone.View.extend
  initialize:->
    console.log "creating taxon observations view"