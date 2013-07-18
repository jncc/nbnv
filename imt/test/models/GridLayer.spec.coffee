define [
	"cs!models/GridLayer"
], (GridLayer)-> 
  describe "GridLayer Model", ->
    gridLayer = null
    
    beforeEach ->
      gridLayer = new GridLayer
  
    it "Deaults should be set correctly", ->
      