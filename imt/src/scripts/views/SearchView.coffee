define [
  "jquery"
  "underscore"
  "backbone"
  "cs!helpers/Globals"
  "select2"
], ($, _, Backbone, Globals)-> Backbone.View.extend

  events: 
    "select2-selecting": "select"
    "change": "clear"

  rows: 20

  initialize: ->
    #Create a select2 infinite scrolling search widget on the input field
    @$el.select2
      placeholder: 'Add species, habitats and site boundaries to the map'
      minimumInputLength: 2
      ajax:
        url: Globals.api "search"
        quietMillis: 100
        data: _.bind( @request, @)
        results: @processResults
      formatResult: _.bind( @renderResult, @)
      dropdownCssClass: "bigdrop"
      escapeMarkup: (m)-> m

  ###
  Populate a query to send to the search end point
  ###
  request: (term, page) -> 
    q: term
    rows: @rows
    start: (page-1)*@rows
    exclude: 'organisation'

  ###
  Assigns an id to each of the results and determines if there are any more
  results available
  ###
  processResults: (data, page) -> 
    record = data.header.start
    _.each data.results, ((result) -> result.id = record++ )

    results: data.results
    more: data.header.numFound > data.header.start + data.header.rows

  ###
  When an option has been selected. Notify the model
  ###
  select: (event, ui) -> @model.addSearchResult event.object
  
  ###
  Clear the selection after it has been handled to show the placeholder
  text again
  ###
  clear: -> @$el.select2 'data', null

  renderResult: (item)-> "<b>#{item.searchMatchTitle}</b><br>#{@formatResult(item)}"

  formatResult: (item) ->
    entityType = item.entityType
    switch item.entityType
      when "taxon" then "Add observations of: #{item.descript}"
      when "taxondataset" then "Add species richness for the dataset: #{item.searchMatchTitle}"
      when "designation" then "Add species richness for the designation: #{item.searchMatchTitle}"
      when "site boundarydataset" then "Add boundaries of: #{item.searchMatchTitle}"
      when "habitatdataset" then "Add habitat: #{item.searchMatchTitle}"
      when "gridsquarefeature" then "Zoom to the grid square: #{item.searchMatchTitle}"
      when "siteboundaryfeature" then "Zoom to the boundary: #{item.searchMatchTitle}, #{item.descript}"