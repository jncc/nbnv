define [
	"cs!models/Picker"
], (Picker)-> 
  describe "Picker Model", ->
    picker = null
    
    beforeEach ->
      picker = new Picker
  
    it "Deaults should be set correctly", ->
      expect(picker.get "isPicking").not.toBeTruthy
      expect(picker.get "wkt").toBe ""