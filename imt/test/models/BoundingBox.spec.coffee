define [
	"cs!models/BoundingBox"
], (BoundingBox)-> 
  describe "BoundingBox Model", ->
    bbox = null
    
    beforeEach ->
      bbox = new BoundingBox
    
    it "Bounding Box Defaults are as expected", ->
      expect(bbox.get "minX").toBe 0
      expect(bbox.get "maxX").toBe 0
      expect(bbox.get "minY").toBe 0
      expect(bbox.get "maxY").toBe 0