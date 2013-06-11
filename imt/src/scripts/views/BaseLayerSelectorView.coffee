define [
  "jquery-ui",
  "underscore",
  "backbone",
  "hbs!templates/BaseLayersSelector"
], ($, _, Backbone, template)-> Backbone.View.extend
  initialize: ->
    do @render
    #$('.selector', @$el).hide()
    @$el.hover (=> $('.others', @$el).stop(true, false).animate height: 152, width: 156, 'fast'),
               (=> $('.others', @$el).stop(true, false).animate height: 73, width: 75, 'fast')
              

    @listenTo @model, "change:baseLayer", @render


  ###
  Each base layer which we allow to set up for displaying on the map has an obvious
  alternative. This obvious alternative should be the most prominant option for the
  currently selected layer. The other non selected layers should be positioned so 
  that their position is easily recallable
  ###
  getTemplateModel: ->
    switch @model.get("baseLayer")
      when "OS"       then return obvious: "Outline", others: _.reject @model.getBaseLayers(), (x)=> x is "Outline" or x is "OS"
      when "Outline"  then return obvious: "OS",      others: _.reject @model.getBaseLayers(), (x)=> x is "OS" or x is "Outline"
      when "Aerial"   then return obvious: "Hybrid",  others: _.reject @model.getBaseLayers(), (x)=> x is "Hybrid" or x is "Aerial"
      when "Hybrid"   then return obvious: "Shaded",  others: _.reject @model.getBaseLayers(), (x)=> x is "Shaded" or x is "Hybrid"
      when "Shaded"   then return obvious: "Hybrid",  others: _.reject @model.getBaseLayers(), (x)=> x is "Hybrid" or x is "Shaded"

  ###
  Event Handler for when the base layer changes
  ###
  render: (evt, baselayer) ->
    @$el.html template do @getTemplateModel
    $("[layer]").each (i, obj) => 
      $(obj).click => @model.set "baseLayer", $(obj).attr "layer"