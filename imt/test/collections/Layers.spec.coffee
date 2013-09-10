define [
	"cs!collections/Layers"
  "cs!models/SingleSpeciesLayer"
  "cs!models/TaxonObservationTypes"
], (Layers, SingleSpeciesLayer, TaxonObservationTypes)-> 
  describe "the layers collection", ->
    layers = null

    beforeEach ->
      layers = new Layers
      
    it "notifies grid layers that auto resolution has changed", ->    
      testLayer = new SingleSpeciesLayer;
      layers.add testLayer

      layers.setZoom 5

      expect(testLayer.get 'autoResolution').toBe "10km"

    it "notifies grid layers that max resolution has changed", ->
      testLayer = new SingleSpeciesLayer;
      layers.add testLayer

      layers.setZoom 13

      expect(testLayer.get 'maxResolution').toBe "100m"
    
    it "can reposition layers which have already been added", ->
      layer1 = new SingleSpeciesLayer
      layer2 = new SingleSpeciesLayer
      layers.add [layer1, layer2]

      layers.position 0, 1

      expect(layers.at(1)).toBe layer1
      expect(layers.at(0)).toBe layer2
    
    it "syncs the max/auto resolution state with new layers", ->
      layers.setZoom 10
      testLayer = new SingleSpeciesLayer

      layers.add testLayer

      expect(testLayer.get 'maxResolution').toBe "1km"
      expect(testLayer.get 'autoResolution').toBe "2km"

    it "adds the given layers absence and/or polygon layers if they are requested", ->
      testLayer = new SingleSpeciesLayer

      spyOn(testLayer, 'getTaxonObservationTypes').andCallFake -> 
        fetch: (callback) -> 
          callback.success new TaxonObservationTypes 
            defaultLayer: testLayer
            hasGridAbsence: true

      layers.add testLayer, addOtherTypes: true

      expect(testLayer.getTaxonObservationTypes).toHaveBeenCalled()
      expect(layers.length).toBe 2
      expect(layers.at(1).get 'isPresence').toBe false

    describe "auto resolution", ->
      it "goes to 10km at zoom level 1", ->
        layers.setZoom(1)
        expect(layers.state.get 'autoResolution').toBe '10km'

      it "goes to 2km at zoom level 9", ->
        layers.setZoom(9)
        expect(layers.state.get 'autoResolution').toBe '2km'

      it "goes to 1km at zoom level 11", ->
        layers.setZoom(11)
        expect(layers.state.get 'autoResolution').toBe '1km'

      it "goes to 100m at zoom level 20", ->
        layers.setZoom(20)
        expect(layers.state.get 'autoResolution').toBe '100m'

    describe "max resolution", ->
      it "goes to Polygon at zoom level 1", ->
        layers.setZoom 1
        expect(layers.state.get 'maxResolution').toBe 'Polygon'

      it "goes to 10km at zoom level 4", ->
        layers.setZoom 4
        expect(layers.state.get 'maxResolution').toBe '10km'

      it "goes to 2km at zoom level 7", ->
        layers.setZoom 7
        expect(layers.state.get 'maxResolution').toBe '2km'

      it "goes to 1km at zoom level 8", ->
        layers.setZoom 8
        expect(layers.state.get 'maxResolution').toBe '1km'

      it "goes to 100m at zoom level 12", ->
        layers.setZoom 12
        expect(layers.state.get 'maxResolution').toBe '100m'