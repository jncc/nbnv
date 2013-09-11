define [
  "cs!helpers/representations/Viewports"
], (Viewports) ->
  describe "Viewports Helper", ->
    it "can shrink bounding box", ->
      toShrink = minX: -30.24235, minY: 23.6345, maxX: 46.2865, maxY: 62.04665
      shrunk = Viewports.shrinkViewport toShrink

      expect(shrunk).toBe "-30.242,23.634,46.286,62.047"

    it "can expand bounding box", ->
      toExpand = "-30.24,46.28,23.63,62.04"
      expanded = Viewports.expandViewport toExpand

      expect(expanded.minX).toBe -30.24
      expect(expanded.minY).toBe 46.28
      expect(expanded.maxX).toBe 23.63
      expect(expanded.maxY).toBe 62.04