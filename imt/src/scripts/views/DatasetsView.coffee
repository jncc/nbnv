define [
  "backbone"
  "underscore"
  "jquery-ui"
], (Backbone, _, $) -> Backbone.View.extend

  organisations: {}

  initialize: ->
    @listenTo @collection, 'add', @addLayer

  addLayer: (layer) ->    
    layer.getDatasets().done (datasets) => 
      datasets.forEach (dataset) => @datasets[dataset.get 'key'] = dataset : dataset
      do @render

  render:->
    @$el.empty()
    _.each @datasets, (val, key) => 
      $('<li>')
        .append("<img src='#{val.dataset.attributes.organisationHref}/logosmall'></img")
        #.html(val.dataset.get "title")
        .appendTo(@$el)