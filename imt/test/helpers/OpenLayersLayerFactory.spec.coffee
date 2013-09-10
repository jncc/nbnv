define [
  "cs!helpers/OpenLayersLayerFactory"
  "cs!models/Layer"
  "cs!models/Picker"
  "openlayers"
], (OpenLayersLayerFactory, Layer, Picker, OpenLayers) ->
  describe "Open layers layer factory", ->
    it "can create bing base layers", ->
      for baseLayer in ["Shaded", "Hybrid", "Aerial"]
        layer = OpenLayersLayerFactory.getBaseLayer baseLayer
        expect(layer.CLASS_NAME).toBe "OpenLayers.Layer.Bing"
        expect(layer.projection.projCode).toBe "EPSG:3857"

    it "can create a OS base layer", ->
      layer = OpenLayersLayerFactory.getBaseLayer "OS"
      expect(layer.projection.projCode).toBe "EPSG:27700"

    it "can create a Outline base layer", ->
      layer = OpenLayersLayerFactory.getBaseLayer "Outline"
      expect(layer.projection.projCode).toBe "EPSG:27700"

    describe "layer creation", ->
      layer = null
      wms = null

      beforeEach ->
        layer = new Layer 
          layer: "testlayer"
          sld : "styles"
          opacity: 1
          visibility : true
          wms: "http://dummy.wms"
        wms = OpenLayersLayerFactory.createLayer layer
        
      it "can create a wms view of a grid layer", ->
        expect(wms.url).toBe "http://dummy.wms"
        expect(wms.params.LAYERS).toEqual ["testlayer"]
        expect(wms.params.SLD_BODY).toBe "styles"
        expect(wms.visibility).toBe true
        expect(wms.opacity).toBe 1

      it "responds to layer change", ->
        layer.set 'layer', "differentlayer"
        expect(wms.params.LAYERS).toEqual ["differentlayer"]

      it "can style the layer", ->
        layer.set 'sld', 'new style'
        expect(wms.params.SLD_BODY).toBe 'new style'

      it "can change the opacity of the layer", ->
        layer.set 'opacity', 0.5
        expect(wms.opacity).toBe 0.5

      it "can change the visibility of the layer", ->
        layer.set 'visibility', false
        expect(wms.visibility).toBe false

      it "can change the url of the layer", ->
        layer.set 'wms', "http://different.wms"
        expect(wms.url).toBe "http://different.wms"

    describe "drawing layer", ->
      picker = null
      map = null
      layer = null

      beforeEach ->
        map = new OpenLayers.Map
        picker = new Picker
        layer = OpenLayersLayerFactory.getDrawingLayer picker
        map.addLayer layer

      it "is created with no features", ->
        expect(layer.features.length).toBe 0
        expect(picker.get "wkt").toBe ''

      it "can update the picker", ->
        wktFactory = new OpenLayers.Format.WKT
        wkt = "POLYGON((-118.34 33.78,-117.95 33.78,-117.95 33.94,-118.34 33.94,-118.34 33.78))"
        layer.addFeatures wktFactory.read wkt
        expect(picker.get "wkt").toBe wkt

      it "only registers the latest feature added to the layer", ->
        wktFactory = new OpenLayers.Format.WKT
        firstWkt = "POLYGON((-118.34 33.78,-117.95 33.78,-117.95 33.94,-118.34 33.94,-118.34 33.78))"
        layer.addFeatures wktFactory.read firstWkt

        secondWkt = "POLYGON((-120.34 32.78,-117.95 32.78,-117.95 32.94,-120.34 32.94,-120.34 32.78))"
        layer.addFeatures wktFactory.read secondWkt
        expect(picker.get "wkt").toBe secondWkt

      it "is updated when the picker changes its wkt", ->
        picker.set "wkt", "POLYGON((-118.34 33.78,-117.95 33.78,-117.95 33.94,-118.34 33.94,-118.34 33.78))"
        expect(layer.features.length).toBe 1