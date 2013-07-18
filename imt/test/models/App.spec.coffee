define [
	"cs!models/App"
], (App)-> 
  describe "App Model", ->
    app = null
    
    beforeEach ->
      app = new App
      
    it "Default values are as expected", ->    
      viewport = app.get "viewport"
      expect(viewport.minX).toBe -14.489099982674913
      expect(viewport.maxX).toBe 7.87906407581859
      expect(viewport.minY).toBe 49.825193671965025
      expect(viewport.maxY).toBe 59.45733404137668

      expect(app.get "baseLayer").toBe "Aerial"
      
      expect(app.get "controlPanelVisible").not.toBeTruthy
      
      layers = app.getBaseLayers()
      expect(layers.length).toBe 5
      expect(layers[0]).toBe "OS"
      expect(layers[1]).toBe "Outline"
      expect(layers[2]).toBe "Shaded"
      expect(layers[3]).toBe "Aerial"
      expect(layers[4]).toBe "Hybrid"      
    
    it "can zoom to spatial search result", ->
      #Given
      spatialSearch = 
        "entityType":"gridsquarefeature"
        "worldBoundingBox":
            "minX":1,"minY":3,"maxX":2,"maxY":4,"epsgCode":"EPSG:4326"

      #When
      app.addSearchResult spatialSearch

      #Then
      viewport = app.get "viewport"
      expect(viewport.minX).toBe 1
      expect(viewport.minY).toBe 3
      expect(viewport.maxX).toBe 2
      expect(viewport.maxY).toBe 4