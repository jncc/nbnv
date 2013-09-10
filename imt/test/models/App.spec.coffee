define [
	"cs!models/App"
], (App)-> 
  describe "App Model", ->
    app = null
    
    beforeEach ->
      app = new App   
    
    it "can zoom to spatial search result", ->
      app.addSearchResult  
        "entityType":"gridsquarefeature"
        "worldBoundingBox":
            "minX":1,"minY":3,"maxX":2,"maxY":4,"epsgCode":"EPSG:4326"

      viewport = app.get "viewport"
      expect(viewport.minX).toBe 1
      expect(viewport.minY).toBe 3
      expect(viewport.maxX).toBe 2
      expect(viewport.maxY).toBe 4

    it "can add a single species layer search result", ->
      app.addSearchResult
        "entityType":"taxon"
        "searchMatchTitle":"Bufo bufo"
        "descript":"Bufo bufo (Linnaeus, 1758), AMPHIBIAN, 16048 record(s)"
        "pExtendedName":"Bufo bufo (Linnaeus, 1758), AMPHIBIAN"
        "taxonVersionKey":"NHMSYS0000080159"
        "name":"Bufo bufo","authority":"(Linnaeus, 1758)"
        "languageKey":"la"
        "taxonOutputGroupKey":"NHMSYS0000080033"
        "taxonOutputGroupName":"amphibian"
        "commonNameTaxonVersionKey":"NBNSYS0000166137"
        "commonName":"Common Toad"
        "organismKey":"NBNORG0000049686"
        "rank":"Species"
        "nameStatus":"Recommended"
        "versionForm":"Well-formed"
        "gatewayRecordCount":16048
        "href":"https://staging-data.nbn.org.uk/Taxa/NHMSYS0000080159"
        "ptaxonVersionKey":"NHMSYS0000080159"

      expect(app.getLayers().length).not.toBe 0

    it "can add a dataset layer search result", ->
      app.addSearchResult
        "entityType":"taxondataset"
        "key":"GA001194"
        "title":"Demonstration dataset for testing dataset importing"

      expect(app.getLayers().length).not.toBe 0

    it "can add a designation layer search result", ->
      app.addSearchResult
        "entityType":"designation"
        "name":"Habitats Directive Annex 4"
        "code":"HABDIR-A4"

      expect(app.getLayers().length).not.toBe 0

    it "can add a site boundary layer search result", ->
      app.addSearchResult
        "entityType":"site boundarydataset"
        "key":"GA000338"
        "title":"Sites of Special Scientific Interest in Wales"

      expect(app.getLayers().length).not.toBe 0

    it "can add a habitat search result", ->
      app.addSearchResult
        "entityType":"habitatdataset"
        "searchMatchTitle":"Woodland BAP priority habitat - England v2.0"
        "key":"HL000005"
        "title":"Woodland BAP priority habitat - England v2.0"

      expect(app.getLayers().length).not.toBe 0