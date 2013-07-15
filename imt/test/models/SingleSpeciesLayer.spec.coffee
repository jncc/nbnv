define [
	"cs!models/SingleSpeciesLayer",
  "cs!models/mixins/TemporalFilterMixin"
], (SingleSpeciesLayer, TemporalFilterMixin)-> 
  describe "SingleSpeciesLayer Model", ->
    singleSpeciesLayer = null
    
    beforeEach ->
      singleSpeciesLayer = new SingleSpeciesLayer
  
    it "Deaults should be set correctly", ->
      expect(singleSpeciesLayer.get "opacity").toBe 1
      expect(singleSpeciesLayer.get "visibility").toBeTruthy
      expect(singleSpeciesLayer.get "resolution").toBe "auto"
      expect(singleSpeciesLayer.get "isPresence").toBeTruthy
      expect(singleSpeciesLayer.get "isPolygon").not.toBeTruthy
      expect(singleSpeciesLayer.get "startDate").toBe TemporalFilterMixin.earliestRecordDate
      expect(singleSpeciesLayer.get "endDate").toBe TemporalFilterMixin.latestRecordDate