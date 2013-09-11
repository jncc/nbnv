define [
  "cs!helpers/representations/BaseLayers"
], (BaseLayers) ->
  describe "Base Layers Helper", ->
    it "can encode os layer", ->
      layerId = BaseLayers.shrinkBaseLayer("OS")
      expect(layerId).toBe("0")

    it "can encode Outline layer", ->
      layerId = BaseLayers.shrinkBaseLayer("Outline")
      expect(layerId).toBe("1")

    it "can encode Shaded layer", ->
      layerId = BaseLayers.shrinkBaseLayer("Shaded")
      expect(layerId).toBe("2")

    it "can encode Aerial layer", ->
      layerId = BaseLayers.shrinkBaseLayer("Aerial")
      expect(layerId).toBe("3")

    it "can encode Hybrid layer", ->
      layerId = BaseLayers.shrinkBaseLayer("Hybrid")
      expect(layerId).toBe("4")

    it "can decode os layer", ->
      layerName = BaseLayers.expandBaseLayer("0")
      expect(layerName).toBe("OS")

    it "can decode Outline layer", ->
      layerName = BaseLayers.expandBaseLayer("1")
      expect(layerName).toBe("Outline")

    it "can decode Shaded layer", ->
      layerName = BaseLayers.expandBaseLayer("2")
      expect(layerName).toBe("Shaded")

    it "can decode Aerial layer", ->
      layerName = BaseLayers.expandBaseLayer("3")
      expect(layerName).toBe("Aerial")

    it "can decode Hybrid layer", ->
      layerName = BaseLayers.expandBaseLayer("4")
      expect(layerName).toBe("Hybrid")