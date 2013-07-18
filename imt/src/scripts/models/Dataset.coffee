define [
  "backbone"
], (Backbone) -> Backbone.Model.extend
  defaults:
    selected: true
  
  idAttribute: "key"
  isSelected: -> @get 'selected'
  getOrganisationID: -> @get 'organisationID'