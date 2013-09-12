define [
  "jquery",
  "underscore",
  "backbone",
  "tpl!templates/BaseLayersSelector.tpl"
], ($, _, Backbone, template)-> Backbone.View.extend
  templateModels :
    "OS" :     obvious: "Outline"
    "Outline": obvious: "OS"
    "Aerial":  obvious: "Hybrid"
    "Hybrid":  obvious: "Shaded"
    "Shaded":  obvious: "Hybrid"

  initialize: ->
    #Populate the [others] value of the templateModels hash, so this does not need to be 
    #processed later
    for key,value of @templateModels
      value.others = _.reject @model.getBaseLayers(), (x)=> x is value.obvious or x is key

    do @render
    @$el.hover (=> $('.others', @$el).stop(true, false).animate height: 152, width: 156, 'fast'),
               (=> $('.others', @$el).stop(true, false).animate height: 73, width: 75, 'fast')
              
    @listenTo @model, "change:baseLayer", @render

  ###
  Each base layer which we allow to set up for displaying on the map has an obvious
  alternative. This obvious alternative should be the most prominant option for the
  currently selected layer. The other non selected layers should be positioned so 
  that their position is easily recallable. The links are defined in templateModels
  ###
  getTemplateModel: -> @templateModels[@model.get("baseLayer")]

  ###
  Event Handler for when the base layer changes
  ###
  render: (evt, baselayer) ->
    @$el.html template do @getTemplateModel
    $("[layer]").each (i, obj) => 
      $(obj).click => @model.set "baseLayer", $(obj).attr "layer"