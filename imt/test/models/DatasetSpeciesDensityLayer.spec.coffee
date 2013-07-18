define [
	"cs!models/DatasetSpeciesDensityLayer",
  "cs!models/mixins/TemporalFilterMixin"
], (DatasetSpeciesDensityLayer, TemporalFilterMixin)-> 
  describe "DatasetSpeciesDensityLayer Model", ->
    datasetSpeciesDensityLayer = null
    
    beforeEach ->
      datasetSpeciesDensityLayer = new DatasetSpeciesDensityLayer
  
    it "Deaults should be set correctly", ->
      expect(datasetSpeciesDensityLayer.get "entityType").toBe "taxondataset"
      expect(datasetSpeciesDensityLayer.get "opacity").toBe 1
      expect(datasetSpeciesDensityLayer.get "visibility").toBeTruthy
      expect(datasetSpeciesDensityLayer.get "resolution").toBe "auto"
      expect(datasetSpeciesDensityLayer.get "isPolygon").not.toBeTruthy
      expect(datasetSpeciesDensityLayer.get "startDate").toBe TemporalFilterMixin.earliestRecordDate
      expect(datasetSpeciesDensityLayer.get "endDate").toBe TemporalFilterMixin.latestRecordDate