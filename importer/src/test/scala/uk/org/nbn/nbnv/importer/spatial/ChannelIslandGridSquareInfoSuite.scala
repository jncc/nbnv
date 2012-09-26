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
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_100m)

    cigr.projection should be ("ED50")
  }

  test("should output an unblurred grid referce") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_100m)

    cigr.gridReference should be (knownGridRef_100m)
  }

  test("1m grid ref should be blured to 100m grid automatically") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_1m)

    cigr.gridReference should be (knownGridRef_100m)
    cigr.gridReferencePrecision should be (100)
  }

  test("10m grid ref should be blured to 100m grid automatically") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_10m)

    cigr.gridReference should be (knownGridRef_100m)
    cigr.gridReferencePrecision should be (100)
  }

  test("100m grid ref should have precision = 100") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_100m)

    cigr.gridReferencePrecision should be (100)
  }

  test("1000m grid ref should have precision = 1000") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_1000m)

    cigr.gridReferencePrecision should be (1000)
  }

  test("2000m DINTY grid ref should have precision = 2000") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_2000m)

    cigr.gridReferencePrecision should be (2000)
  }

  test("10000m grid ref should have precision = 10000") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_10000m)

    cigr.gridReferencePrecision should be (10000)
  }

  test("should blur 100m grid ref to 1000m") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_100m, 1000)

    cigr.gridReference should be (knownGridRef_1000m)
    cigr.gridReferencePrecision should be(1000)
  }

  test("should blur 100m grid ref to 2000m") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_100m, 2000)

    cigr.gridReference should be (knownGridRef_2000m)
    cigr.gridReferencePrecision should be (2000)
  }

  test("should blur 100m grid ref to 10000m") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_100m, 10000)

    cigr.gridReference should be (knownGridRef_10000m)
    cigr.gridReferencePrecision should be (10000)
  }

  test("should blur DINTY grid ref to 10000m") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_2000m, 10000)

    cigr.gridReference should be (knownGridRef_10000m)
    cigr.gridReferencePrecision should be (10000)
  }

  test("should normailise precision of 30 to 100m") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_100m, 30)

    cigr.gridReferencePrecision should be (100)
    cigr.gridReference should be (knownGridRef_100m)
  }

  test("should normailise precision of 150 to 1000m") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_100m, 150)

    cigr.gridReferencePrecision should be (1000)
    cigr.gridReference should be (knownGridRef_1000m)
  }

  test("should normailise precision of 1200 to 2000m") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_1000m, 1200)

    cigr.gridReferencePrecision should be (2000)
    cigr.gridReference should be (knownGridRef_2000m)
  }

  test("should normailise precision of 8000 to 10000m") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_2000m, 8000)

    cigr.gridReferencePrecision should be (10000)
    cigr.gridReference should be (knownGridRef_10000m)
  }

  test("should give 1000m grid square as parent of 100m") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_100m)

    cigr.getParentGridRef match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_1000m)
        parent.gridReferencePrecision should be (1000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should give 2000m grid square as parent of 1000m") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_1000m)

    cigr.getParentGridRef match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_2000m)
        parent.gridReferencePrecision should be (2000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should give 10000m grid square as parent of 2000m") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_2000m)

    cigr.getParentGridRef match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_10000m)
        parent.gridReferencePrecision should be (10000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should be no parent of 10000m grid square") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_10000m)

    cigr.getParentGridRef should be (None)
  }

  test("should give WKT for 100m grid square") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_100m)

    cigr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 1000m grid square") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_1000m)

    cigr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 2000m grid square") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_2000m)

    cigr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 10000m grid square") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_10000m)

    cigr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give 100m grid ref at 2000m") {
    val cigr = new ChannelIslandGridSquareInfo(knownGridRef_100m)

    val lowerCigr = cigr.getLowerPrecisionGridRef(2000)

    lowerCigr should not be (null)
    lowerCigr.gridReference should be (knownGridRef_2000m)
    lowerCigr.gridReferencePrecision should be (2000)
  }
}
