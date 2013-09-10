define [
  "cs!models/HabitatLayer"
], (HabitatLayer)-> 
  describe "HabitatLayer", ->
    it "acknowledges the dataset with the same key as it", ->
      layer = new HabitatLayer key: "HL000005"
      usedDatasets = layer.getUsedDatasets()
      expect(usedDatasets.length).toBe 1
      expect(usedDatasets[0].id).toBe "HL000005"