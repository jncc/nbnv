define [
  "jquery",
  "backbone",
  "cs!views/TaxonObservationsView"
  "hbs!templates/PickerView"
], ($, Backbone, TaxonObservationsView, template) -> Backbone.View.extend

  initialize: ->
    @listenTo @model, 'change:resultsForLayers', @render

  render: ->
    resultsForLayers = @model.getResultsForLayers()

    @$el.html template resultsForLayers

    _.each resultsForLayers, (resultsForLayer, index) =>
      new TaxonObservationsView
        collection: resultsForLayer.records
        el: @$("li:eq(#{index}) div.records")
