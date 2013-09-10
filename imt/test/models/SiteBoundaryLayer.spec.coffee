define [
  "cs!models/SiteBoundaryLayer"
], (SiteBoundaryLayer)-> 
  describe "SiteBoundaryLayer", ->
    it "acknowledges the dataset with the same key as it", ->
      layer = new SiteBoundaryLayer key: "GA000338"
      usedDatasets = layer.getUsedDatasets()
      expect(usedDatasets.length).toBe 1
      expect(usedDatasets[0].id).toBe "GA000338"
