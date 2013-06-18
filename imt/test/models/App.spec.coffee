define [
	"cs!models/App"
], (App)-> 
  describe "App Model", ->
    it "can zoom to spatial search result", ->
      #Given
      app = new App
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