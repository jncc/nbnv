define [
  "cs!helpers/representations/Years"
], (Years) ->
  describe "Year Ranger Helper", ->
    it "can represent the year range 1966-1997", ->
      range = startDate: 1966, endDate: 1997
      shrunkYear = Years.shrinkYearRange range

      expect(shrunkYear).toBe "jsD"

    it "can represent the current full range of nbn gateway records", ->
      range = startDate: 1600, endDate: 2013
      shrunkYear = Years.shrinkYearRange range

      expect(shrunkYear).toBe "6T"

    it "can expand the year range 1966-1997", ->
      range = Years.expandYearRange "jsD"

      expect(range.startDate).toBe 1966
      expect(range.endDate).toBe 1997

    it "can expand the current full range of nbn gateway records", ->
      range = Years.expandYearRange "6T"

      expect(range.startDate).toBe 1600
      expect(range.endDate).toBe 2013

    it "can shrink a date range 50 years in the future", ->
      range = startDate: 2063, endDate: 2090
      shrunkYear = Years.shrinkYearRange range

      expect(shrunkYear).toBe "v/g"

    it "can expand a date range 50 years in the future", ->
      range = Years.expandYearRange "v/g"

      expect(range.startDate).toBe 2063
      expect(range.endDate).toBe 2090