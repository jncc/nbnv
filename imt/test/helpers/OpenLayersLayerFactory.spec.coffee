define [
  "cs!helpers/OpenLayersLayerFactory"
  "cs!models/Layer"
], (OpenLayersLayerFactory, Layer) ->
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