define [
  "jquery"
  "backbone"
  "hbs!templates/TaxonObservations"
  "DataTables"
], ($, Backbone, template) -> Backbone.View.extend
  initialize:->
    @listenTo @collection, 'sync', @render

  render: ->
    @$el.html template @collection.toJSON()
    @$('table').dataTable
      "bJQueryUI": true
      "bLengthChange": false
      "oLanguage":
        "sSearch": "Search within these results:"