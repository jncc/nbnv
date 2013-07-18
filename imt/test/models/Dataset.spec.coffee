define [
	"cs!models/Dataset"
], (Dataset)-> 
  describe "Dataset Model", ->
    dataset = null
    
    beforeEach ->
      dataset = new Dataset
  
    it "Deaults should be set correctly", ->
      expect(dataset.get "selected").toBeTruthy