define [
  "cs!models/DatasetSpeciesDensityLayer"
], (DatasetSpeciesDensityLayer)-> 
  describe "DatasetSpeciesDensityLayer", ->
    it "acknowledges the dataset with the same key as it", ->
      layer = new DatasetSpeciesDensityLayer key: "GA000455"
      usedDatasets = layer.getUsedDatasets()
      expect(usedDatasets.length).toBe 1
      expect(usedDatasets[0].id).toBe "GA000455"

    it "can filter start year", ->
      layer = new DatasetSpeciesDensityLayer key: "GA000455", startDate: 2012
      expect(layer.getWMS()).toContain "startyear=2012"

    it "can filter end year", ->
      layer = new DatasetSpeciesDensityLayer key: "GA000455", endDate: 2012
      expect(layer.getWMS()).toContain "endyear=2012"

    it "doesnt apply a temporal filter when default years are used", ->
      layer = new DatasetSpeciesDensityLayer key: "GA000455"
      expect(layer.getWMS()).not.toContain "endyear"
      expect(layer.getWMS()).not.toContain "startyear"