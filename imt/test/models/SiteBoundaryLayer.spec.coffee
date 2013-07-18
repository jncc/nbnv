define [
	"cs!models/SiteBoundaryLayer"
], (SiteBoundaryLayer)-> 
  describe "SiteBoundaryLayer Model", ->
    siteBoundaryLayer = null
    
    beforeEach ->
      siteBoundaryLayer = new SiteBoundaryLayer
  
    it "Deaults should be set correctly", ->
      expect(siteBoundaryLayer.get "entityType").toBe "site boundarydataset"
      expect(siteBoundaryLayer.get "opacity").toBe 1
      expect(siteBoundaryLayer.get "visibility").toBeTruthy
#      expect(siteBoundaryLayer.get "wms").get "Globals".gis.toBe "SiteBoundaryDatasets"
      expect(siteBoundaryLayer.get "symbol").toBe "fill"