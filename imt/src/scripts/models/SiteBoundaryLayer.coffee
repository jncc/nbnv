define [
  "jquery-md5"
  "cs!models/Layer"
  "cs!models/Dataset"
  "cs!helpers/Globals"
  "hbs!templates/slds/Default"
], ($, Layer, Dataset, Globals, sld) -> Layer.extend
  defaults:
    entityType: 'site boundarydataset'
    opacity: 1
    wms: Globals.gis "SiteBoundaryDatasets"

  url: -> Globals.api "siteBoundaryDatasets/#{@id}"

  idAttribute: "key"

  initialize: ()->
    @set "name", @attributes.title
    @set "layer", @id
    @set "colour", $.md5(@id).substring 0, 6
        
    @on 'change:colour', -> @trigger 'change:legendIcon'
    @on 'change:colour change:layer', -> @trigger 'change:sld'

  getLegendIcon: ->
    backgroundColor: "#" + @get "colour"

  getSLD: -> sld
    layer: @getLayer(),
    colour: @get "colour"

  getUsedDatasets: -> [ new Dataset @attributes ]