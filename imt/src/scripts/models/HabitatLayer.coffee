define [
  "underscore"
  "cs!models/Layer"
  "cs!models/Dataset"
  "cs!models/mixins/PolygonFillMixin"
  "cs!helpers/Globals"
], (_, Layer, Dataset, PolygonFillMixin, Globals) -> Layer.extend _.extend {}, PolygonFillMixin,
  defaults:
    entityType: 'habitatdataset'
    opacity: 1
    visibility: true
    wms: Globals.gis "HabitatDatasets"
    symbol: "fill"

  url: -> Globals.api "habitatDatasets/#{@id}"
  
  idAttribute: "key"

  initialize: ()->
    @set "layer", @id
    @on "change:title", -> @trigger "change:name"
    PolygonFillMixin.initialize.call(this, arguments); #initalize the mixin

  getName: -> @get "title"

  getUsedDatasets: -> [ new Dataset @attributes ]