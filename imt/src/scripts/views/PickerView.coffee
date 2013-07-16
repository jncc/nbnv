define [
  "jquery",
  "backbone",
  "cs!views/TaxonObservationsView"
  "hbs!templates/PickerView"
  "jquery-ui"
], ($, Backbone, TaxonObservationsView, template) -> Backbone.View.extend

  events:
    "click .clearResults" : "clearResults"

  initialize: ->
    do @render

    @listenTo @model, 'change:resultsForLayers', @render

  clearResults: -> do @model.clearResults

  render: ->
    @$el.html template hasResults: @model.hasResults()

    if @model.hasResults()
      resultsDiv = @$('.results')
      resultsForLayers = @model.getResultsForLayers()

      _.each resultsForLayers, (resultsForLayer, index) =>
        resultsDiv.append @createHeader resultsForLayer

        #could perform some logic here to use a different view if appropriate
        new TaxonObservationsView
          collection: resultsForLayer.records
          el: $('<div class="records">').appendTo(resultsDiv)
      
      resultsDiv.accordion
        heightStyle: "content"
        collapsible: true

  ###
  Create a header for the accordian, this should probably live as its own view
  ###
  createHeader: (resultsForLayer)->
    #add a loading icon when loading. remove this when records has synced
    loadingIcon = $('<img class="loading" src="img/ajax-loader.gif">')
    resultsForLayer.records.on 'sync', -> do loadingIcon.remove

    $('<h3>')
      .append($('<span class="icon">').css resultsForLayer.layer.getLegendIcon())
      .append(resultsForLayer.layer.getName())
      .append(loadingIcon)