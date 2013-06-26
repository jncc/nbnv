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
    wms: Globals.gis "HabitatDatasets"
    symbol: "fill"

  url: -> Globals.api "habitatDatasets/#{@id}"
  
  idAttribute: "key"

  initialize: ()->
    @set "name", @attributes.title
    @set "layer", @id    
    PolygonFillMixin.initialize.call(this, arguments); #initalize the mixin

  getUsedDatasets: -> [ new Dataset @attributes ]