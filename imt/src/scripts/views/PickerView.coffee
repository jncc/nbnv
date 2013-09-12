define [
  "jquery",
  "backbone",
  "cs!views/TaxonObservationsView"
  "hbs!templates/PickerView"
  "jquery.ui.accordion"
], ($, Backbone, TaxonObservationsView, template) -> Backbone.View.extend

  events:
    "click .clearResults" : "clearResults"

  initialize: ->
    @picker = @model.getPicker() #maintain a reference to the picker

    do @render

    @listenTo @picker, 'change:resultsForLayers', @render
    @listenTo @model.getCurrentUser(), 'change', @render

  clearResults: -> 
    do @picker.clearResults
    @picker.set 'isPicking', true #put the map back into picking mode

  render: ->
    @$el.html template 
      hasResults: @picker.hasResults()
      loggedIn: @model.getCurrentUser().isLoggedIn()

    if @picker.hasResults()
      @picker.set 'isPicking', false # now we have some results, disable picking
      resultsDiv = @$('.results')
      resultsForLayers = @picker.getResultsForLayers()

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