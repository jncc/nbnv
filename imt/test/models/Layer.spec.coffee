define [
	"cs!models/Layer"
], (Layer)-> 
  describe "Layer Model", ->
    layer = null
    
    beforeEach ->
      layer = new Layer
  
    it "Deaults should be set correctly", ->
      