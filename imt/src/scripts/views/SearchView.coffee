define [
  "jquery-ui",
  "underscore",
  "backbone",
  "text!templates/SearchView.html"
], ($, _, Backbone, template)-> Backbone.View.extend

  initialize: ->
    @$el.html template
    #Start a new search 
    @searchCollection = do @model.getSearch

    #Create a jquery autocomplete widget. Use 
    $('input', @$el).autocomplete 
      source: _.bind(@search, @),
      select: _.bind(@select, @)
    .data('uiAutocomplete')._renderItem = _.bind(@renderResult,@)
    

  select: (event, ui) ->
    @model.addSearchResult ui.item

  search: (request, response) ->
    @searchCollection
      .fetch
        data : 
          q:request.term, 
          rows:3
      .success => 
        response @searchCollection.map (model)-> model.attributes

  renderResult: (ul, item)->

    $('<li>')
      .append( "<a>#{@formatResult(item)}</a>" )
      .appendTo ul

  formatResult: (item) ->
    entityType = item.entityType
    switch item.entityType
      when "taxon" then "Add occurrence of: #{item.searchMatchTitle}"
      when "taxondataset" then "Add species richness for the dataset: #{item.searchMatchTitle}"
      when "designation" then "Add species richness for the designation: #{item.searchMatchTitle}"
      when "site boundarydataset" then "Add boundaries of: #{item.searchMatchTitle}"
      when "habitatdataset" then "Add habitat: #{item.searchMatchTitle}"
      when "gridsquarefeature" then "Zoom to the grid square: #{item.searchMatchTitle}"
      when "siteboundaryfeature" then "Zoom to the boundary: #{item.searchMatchTitle}"

  