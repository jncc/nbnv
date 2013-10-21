define [
  "jquery"
  "backbone"
  "tpl!templates/TaxonObservations.tpl"
  "DataTables"
], ($, Backbone, template) -> Backbone.View.extend
  initialize:->
    @listenTo @collection, 'sync', @render

  render: ->
    @$el.html template
      observations: @collection.toJSON()
      isFilteredByTaxon: @collection.isFilteredByTaxon()
      
    @$('table').dataTable
      "bJQueryUI": true
      "bLengthChange": false
      "oLanguage":
        "sSearch": "Search within these results:"