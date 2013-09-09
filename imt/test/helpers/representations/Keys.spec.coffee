define [
  "cs!helpers/representations/Keys"
  "cs!helpers/Numbers"
], (Keys, Numbers) ->
  describe "Keys Helper", ->
    describe "tvk part", ->
      it "can expand a normal taxon version key", ->
        key = "NBNSYS0000037976"
        expandedKey = Keys.expandTVK key
        expect(expandedKey).toBe key

      it "can shrink norfolk hawker key", ->
        shrunkenKey = Keys.shrinkTVK "NBNSYS0000005629"
        expect(shrunkenKey).toBe "5Vr"

      it "can expand norfolk hawker key", ->
        expandedKey = Keys.expandTVK "5Vr"
        expect(expandedKey).toBe "NBNSYS0000005629"

      it "can shrink unsupported tvk", ->
        key = "MYWEIRD_TAXONKEY"
        shrunkenKey = Keys.shrinkTVK key
        expect(shrunkenKey).toBe key

      shrinkExpandTVK = (type)->
        for i in [0..10000]
          key = "#{type}SYS#{Numbers.pad i, 10}" 
          expandedKey = Keys.expandTVK Keys.shrinkTVK key
          expect(key).toBe expandedKey

      it "can shrink/expand NHM keys", -> shrinkExpandTVK "NHM"
      it "can shrink/expand NBN keys", -> shrinkExpandTVK "NBN"
      it "can shrink/expand BMS keys", -> shrinkExpandTVK "BMS"
      it "can shrink/expand EHS keys", -> shrinkExpandTVK "EHS"

    describe "datasetkey part", ->
      it "can expand a normal dataset key", ->
        key = "GA000466"
        expandedKey = Keys.expandDatasetKey key
        expect(expandedKey).toBe key

      it "can shrink NBN test datasetkey", ->
        shrunkenKey = Keys.shrinkDatasetKey "GA000466"
        expect(shrunkenKey).toBe "T9"

      it "can expand NBN test datasetkey", ->
        expandedKey = Keys.expandDatasetKey "T9"
        expect(expandedKey).toBe "GA000466"

      it "can shrink unsupported datasetkey", ->
        key = "MYDATASET"
        shrunkenKey = Keys.shrinkDatasetKey key
        expect(shrunkenKey).toBe key

      shrinkExpandDatasetKey = (type)->
        for i in [0..10000]
          key = "#{type}#{Numbers.pad i, 6}" 
          expandedKey = Keys.expandDatasetKey Keys.shrinkDatasetKey key
          expect(key).toBe expandedKey

      it "can shrink/expand GA keys", -> shrinkExpandDatasetKey "GA"
      it "can shrink/expand HL keys", -> shrinkExpandDatasetKey "HL"
      it "can shrink/expand SB keys", -> shrinkExpandDatasetKey "SB"