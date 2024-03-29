package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite

class ChannelIslandGridSquareInfoSuite extends BaseFunSuite {
  val knownGridRef_1m = "WV1663471237"
  val knownGridRef_10m = "WV16637123"
  val knownGridRef_100m = "WV166712"
  val knownGridRef_1000m = "WV1671"
  val knownGridRef_2000m = "WV17Q"
  val knownGridRef_10000m = "WV17"

  test("should identify projection as ED50") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_100m)

    cigr.projection should be ("ED50")
  }

  test("shold identify EPSG code as 23030") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_100m)

    cigr.epsgCode should be (23030)
  }

  test("should output an unblurred grid referce") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_100m)

    cigr.gridReference should be (knownGridRef_100m)
  }

  test("1m grid ref should be blured to 100m grid automatically") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_1m)

    cigr.gridReference should be (knownGridRef_100m)
    cigr.gridReferencePrecision should be (100)
  }

  test("10m grid ref should be blured to 100m grid automatically") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_10m)

    cigr.gridReference should be (knownGridRef_100m)
    cigr.gridReferencePrecision should be (100)
  }

  test("100m grid ref should have precision = 100") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_100m)

    cigr.gridReferencePrecision should be (100)
  }

  test("1000m grid ref should have precision = 1000") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_1000m)

    cigr.gridReferencePrecision should be (1000)
  }

  test("2000m DINTY grid ref should have precision = 2000") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_2000m)

    cigr.gridReferencePrecision should be (2000)
  }

  test("10000m grid ref should have precision = 10000") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_10000m)

    cigr.gridReferencePrecision should be (10000)
  }

  test("should blur 100m grid ref to 1000m") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_100m, Some(1000))

    cigr.gridReference should be (knownGridRef_1000m)
    cigr.gridReferencePrecision should be(1000)
  }

  test("should blur 100m grid ref to 2000m") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_100m, Some(2000))

    cigr.gridReference should be (knownGridRef_2000m)
    cigr.gridReferencePrecision should be (2000)
  }

  test("should blur 100m grid ref to 10000m") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_100m, Some(10000))

    cigr.gridReference should be (knownGridRef_10000m)
    cigr.gridReferencePrecision should be (10000)
  }

  test("should blur DINTY grid ref to 10000m") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_2000m, Some(10000))

    cigr.gridReference should be (knownGridRef_10000m)
    cigr.gridReferencePrecision should be (10000)
  }

  test("should normailise precision of 30 to 100m") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_100m, Some(30))

    cigr.gridReferencePrecision should be (100)
    cigr.gridReference should be (knownGridRef_100m)
  }

  test("should normailise precision of 150 to 1000m") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_100m, Some(150))

    cigr.gridReferencePrecision should be (1000)
    cigr.gridReference should be (knownGridRef_1000m)
  }

  test("should normailise precision of 1200 to 2000m") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_1000m, Some(1200))

    cigr.gridReferencePrecision should be (2000)
    cigr.gridReference should be (knownGridRef_2000m)
  }

  test("should normailise precision of 8000 to 10000m") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_2000m, Some(8000))

    cigr.gridReferencePrecision should be (10000)
    cigr.gridReference should be (knownGridRef_10000m)
  }

  test("should give 1000m grid square as parent of 100m") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_100m)

    cigr.getParentGridSquareInfo match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_1000m)
        parent.gridReferencePrecision should be (1000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should give 2000m grid square as parent of 1000m") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_1000m)

    cigr.getParentGridSquareInfo match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_2000m)
        parent.gridReferencePrecision should be (2000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should give 10000m grid square as parent of 2000m") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_2000m)

    cigr.getParentGridSquareInfo match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_10000m)
        parent.gridReferencePrecision should be (10000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should be no parent of 10000m grid square") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_10000m)

    cigr.getParentGridSquareInfo should be (None)
  }

  test("should give WKT for 100m grid square in WGS84") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_100m)

    cigr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 1000m grid square in WGS84") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_1000m)

    cigr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 2000m grid square in WGS84") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_2000m)

    cigr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 10000m grid square in WGS84") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_10000m)

    cigr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should compute 2000m grid ref from 100m grid ref") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_100m)

    val lowerCigr = cigr.getLowerPrecisionGridSquareInfo(2000)

    lowerCigr should not be (null)
    lowerCigr.gridReference should be (knownGridRef_2000m)
    lowerCigr.gridReferencePrecision should be (2000)
  }

  test("should compute 10000m grid ref from 1000m grid ref") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_1000m)

    val lowerCigr = cigr.getLowerPrecisionGridSquareInfo(10000)

    lowerCigr should not be (null)
    lowerCigr.gridReference should be (knownGridRef_10000m)
    lowerCigr.gridReferencePrecision should be (10000)
  }

  test("should return same grid square if requested precision is lower") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_1000m)

    val lowerCigr = cigr.getLowerPrecisionGridSquareInfo(100)

    lowerCigr should be (cigr)
  }

  test("should return same grid square if requested precision is the same") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_1000m)

    val lowerCigr = cigr.getLowerPrecisionGridSquareInfo(1000)

    lowerCigr should be (cigr)
  }

  test("should give WKT for 100m grid square") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_100m)

    cigr.sourceProjectionPolygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 1000m grid square") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_1000m)

    cigr.sourceProjectionPolygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 2000m grid square") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_2000m)

    cigr.sourceProjectionPolygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 10000m grid square") {
    val cigr = ChannelIslandGridSquareInfo(knownGridRef_10000m)

    cigr.sourceProjectionPolygon matches (TestResources.polygonWKTRegex)
  }

  //Add some tests in for easting northing to grid ref conversion and back again from real
  //values verrified externally

  test("should give correct easting and norhting for WV1663471237") {
    val cigr = ChannelIslandGridSquareInfo("WV1663471237")

    val (easting, northing) = cigr.getEastingNorthing

    easting should be (516600)
    northing should be (5471200)
  }

  test("should give correct grid gref for easting 516600 & northing 5471200") {
    val cigr = ChannelIslandGridSquareInfo(516600, 5471200)

    cigr.gridReference should be ("WV166712")
  }


}
