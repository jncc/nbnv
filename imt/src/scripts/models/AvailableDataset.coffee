define [
  "backbone"
], (Backbone) -> Backbone.Model.extend
  idAttribute: "key"
  isSelected: -> @get 'selected'
  getOrganisationID: -> @get 'organisationID'