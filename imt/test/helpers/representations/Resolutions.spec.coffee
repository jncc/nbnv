define [
  "cs!helpers/representations/Resolutions"
], (Resolutions) ->
  describe "Resolutions Helper", ->
    shrinkExpandGrid = (isPresence) ->
      for res in ['10km', '2km', '1km', '100m', 'auto']
        toShrink = resolution: res, isPresence: isPresence

        shrunk = Resolutions.shrinkResolution toShrink
        expand = Resolutions.expandResolution shrunk
        
        expect(expand.resolution).toBe toShrink.resolution
        expect(expand.isPresence).toBe toShrink.isPresence


    it "can shrink/expand grid, presence", -> shrinkExpandGrid true
    it "can shrink/expand grid, absence", -> shrinkExpandGrid false

    it "can shrink/expand polygon, presence", ->
      shrunk = Resolutions.shrinkResolution isPresence: true, isPolygon: true
      expand = Resolutions.expandResolution shrunk
      
      expect(expand.isPolygon).toBe true
      expect(expand.isPresence).toBe true

    it "can shrink/expand polygon, absence",->
      shrunk = Resolutions.shrinkResolution isPresence: false, isPolygon: true
      expand = Resolutions.expandResolution shrunk
      
      expect(expand.isPolygon).toBe true
      expect(expand.isPresence).toBe false

    it "can shrink 2km absence", ->
      shrunken = Resolutions.shrinkResolution resolution: '2km', isPresence: false

      expect(shrunken).toBe "2"