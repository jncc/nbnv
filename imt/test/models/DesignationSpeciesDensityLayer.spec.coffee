define [
	"cs!models/DesignationSpeciesDensityLayer",
  "cs!models/mixins/TemporalFilterMixin"
], (DesignationSpeciesDensityLayer, TemporalFilterMixin)-> 
  describe "DesignationSpeciesDensityLayer Model", ->
    designationSpeciesDensityLayer = null
    
    beforeEach ->
      designationSpeciesDensityLayer = new DesignationSpeciesDensityLayer
  
    it "Deaults should be set correctly", ->
      expect(designationSpeciesDensityLayer.get "entityType").toBe "designation"
      expect(designationSpeciesDensityLayer.get "opacity").toBe 1
      expect(designationSpeciesDensityLayer.get "visibility").toBeTruthy
      expect(designationSpeciesDensityLayer.get "resolution").toBe "auto"
      expect(designationSpeciesDensityLayer.get "isPolygon").not.toBeTruthy
      expect(designationSpeciesDensityLayer.get "startDate").toBe TemporalFilterMixin.earliestRecordDate
      expect(designationSpeciesDensityLayer.get "endDate").toBe TemporalFilterMixin.latestRecordDate