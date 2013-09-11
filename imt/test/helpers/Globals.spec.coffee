define [
  "cs!helpers/Globals"
], (Globals)-> 
  describe "globals helper", ->
    it "can build an api url", ->
      Globals.servers = api: "dummy.api"
      path = Globals.api "testPath"
      expect(path).toBe "https://dummy.api/testPath?callback=?"

    it "can build a gis url", ->
      Globals.servers = gis: "dummy.gis"
      path = Globals.gis("testPath", startYear: 1920, endYear: 1940)
      expect(path).toBe "https://dummy.gis/testPath?startYear=1920&endYear=1940"

    it "can build a portal url", ->
      Globals.servers = portal: "dummy.portal"
      path = Globals.portal "testPath"
      expect(path).toBe "https://dummy.portal/testPath"