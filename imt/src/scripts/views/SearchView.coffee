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
    .data('uiAutocomplete')._renderItem = @renderResult

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
      .append( "<a>#{item.searchMatchTitle}</a>" )
      .appendTo ul