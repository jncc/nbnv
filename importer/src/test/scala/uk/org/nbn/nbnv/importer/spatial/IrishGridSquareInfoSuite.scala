package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite

class IrishGridSquareInfoSuite extends BaseFunSuite {
  val knownGridRef_1m = "A1663471237"
  val knownGridRef_10m = "A16637123"
  val knownGridRef_100m = "A166712"
  val knownGridRef_1000m = "A1671"
  val knownGridRef_2000m = "A17Q"
  val knownGridRef_10000m = "A17"

  test("should identify projection as OSNI") {
    val igr = new IrishGridSquareInfo(knownGridRef_100m)

    igr.projection should be ("OSNI")
  }

  test("should identify EPSG code as 29903") {
    val igr = new IrishGridSquareInfo(knownGridRef_100m)

    igr.epsgCode should be ("29903")
  }

  test("should output an unblurred grid referce") {
    val igr = new IrishGridSquareInfo(knownGridRef_100m)

    igr.gridReference should be (knownGridRef_100m)
  }

  test("1m grid ref should be blured to 100m grid automatically") {
    val igr = new IrishGridSquareInfo(knownGridRef_1m)

    igr.gridReference should be (knownGridRef_100m)
    igr.gridReferencePrecision should be (100)
  }

  test("10m grid ref should be blured to 100m grid automatically") {
    val igr = new IrishGridSquareInfo(knownGridRef_10m)

    igr.gridReference should be (knownGridRef_100m)
    igr.gridReferencePrecision should be (100)
  }

  test("100m grid ref should have precision = 100") {
    val igr = new IrishGridSquareInfo(knownGridRef_100m)

    igr.gridReferencePrecision should be (100)
  }

  test("1000m grid ref should have precision = 1000") {
    val igr = new IrishGridSquareInfo(knownGridRef_1000m)

    igr.gridReferencePrecision should be (1000)
  }

  test("2000m DINTY grid ref should have precision = 2000") {
    val igr = new IrishGridSquareInfo(knownGridRef_2000m)

    igr.gridReferencePrecision should be (2000)
  }

  test("10000m grid ref should have precision = 10000") {
    val igr = new IrishGridSquareInfo(knownGridRef_10000m)

    igr.gridReferencePrecision should be (10000)
  }

  test("should blur 100m grid ref to 1000m") {
    val igr = new IrishGridSquareInfo(knownGridRef_100m, 1000)

    igr.gridReference should be (knownGridRef_1000m)
    igr.gridReferencePrecision should be(1000)
  }

  test("should blur 100m grid ref to 2000m") {
    val igr = new IrishGridSquareInfo(knownGridRef_100m, 2000)

    igr.gridReference should be (knownGridRef_2000m)
    igr.gridReferencePrecision should be (2000)
  }

  test("should blur 100m grid ref to 10000m") {
    val igr = new IrishGridSquareInfo(knownGridRef_100m, 10000)

    igr.gridReference should be (knownGridRef_10000m)
    igr.gridReferencePrecision should be (10000)
  }

  test("should blur DINTY grid ref to 10000m") {
    val igr = new IrishGridSquareInfo(knownGridRef_2000m, 10000)

    igr.gridReference should be (knownGridRef_10000m)
    igr.gridReferencePrecision should be (10000)
  }

  test("should normailise precision of 30 to 100m") {
    val igr = new IrishGridSquareInfo(knownGridRef_100m, 30)

    igr.gridReferencePrecision should be (100)
    igr.gridReference should be (knownGridRef_100m)
  }

  test("should normailise precision of 150 to 1000m") {
    val igr = new IrishGridSquareInfo(knownGridRef_100m, 150)

    igr.gridReferencePrecision should be (1000)
    igr.gridReference should be (knownGridRef_1000m)
  }

  test("should normailise precision of 1200 to 2000m") {
    val igr = new IrishGridSquareInfo(knownGridRef_1000m, 1200)

    igr.gridReferencePrecision should be (2000)
    igr.gridReference should be (knownGridRef_2000m)
  }

  test("should normailise precision of 8000 to 10000m") {
    val igr = new IrishGridSquareInfo(knownGridRef_2000m, 8000)

    igr.gridReferencePrecision should be (10000)
    igr.gridReference should be (knownGridRef_10000m)
  }

  test("should give 1000m grid square as parent of 100m") {
    val igr = new IrishGridSquareInfo(knownGridRef_100m)

    igr.getParentGridSquareInfo match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_1000m)
        parent.gridReferencePrecision should be (1000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should give 2000m grid square as parent of 1000m") {
    val igr = new IrishGridSquareInfo(knownGridRef_1000m)

    igr.getParentGridSquareInfo match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_2000m)
        parent.gridReferencePrecision should be (2000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should give 10000m grid square as parent of 2000m") {
    val igr = new IrishGridSquareInfo(knownGridRef_2000m)

    igr.getParentGridSquareInfo match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_10000m)
        parent.gridReferencePrecision should be (10000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should be no parent of 10000m grid square") {
    val igr = new IrishGridSquareInfo(knownGridRef_10000m)

    igr.getParentGridSquareInfo should be (None)
  }

  test("should give WKT for 100m grid square in WGS84") {
    val igr = new IrishGridSquareInfo(knownGridRef_100m)

    igr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 1000m grid square in WGS84") {
    val igr = new IrishGridSquareInfo(knownGridRef_1000m)

    igr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 2000m grid square in WGS84") {
    val igr = new IrishGridSquareInfo(knownGridRef_2000m)

    igr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 10000m grid square in WGS84") {
    val igr = new IrishGridSquareInfo(knownGridRef_10000m)

    igr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }
  
  test("should compute 2000m grid ref from 100m grid ref") {
    val igr = new IrishGridSquareInfo(knownGridRef_100m)

    val lowerIgr = igr.getLowerPrecisionGridSquareInfo(2000)

    lowerIgr should not be (null)
    lowerIgr.gridReference should be (knownGridRef_2000m)
    lowerIgr.gridReferencePrecision should be (2000)
  }

  test("should give WKT for 100m grid square") {
    val igr = new IrishGridSquareInfo(knownGridRef_100m)

    igr.sourceProjectionPolygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 1000m grid square") {
    val igr = new IrishGridSquareInfo(knownGridRef_1000m)

    igr.sourceProjectionPolygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 2000m grid square") {
    val igr = new IrishGridSquareInfo(knownGridRef_2000m)

    igr.sourceProjectionPolygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 10000m grid square") {
    val igr = new IrishGridSquareInfo(knownGridRef_10000m)

    igr.sourceProjectionPolygon matches (TestResources.polygonWKTRegex)
  }
}
