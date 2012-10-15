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

    igr.epsgCode should be (29903)
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

  test("should compute 10000m grid ref from 1000m grid ref") {
    val igr = new IrishGridSquareInfo(knownGridRef_1000m)

    val lowerIgr = igr.getLowerPrecisionGridSquareInfo(10000)

    lowerIgr should not be (null)
    lowerIgr.gridReference should be (knownGridRef_10000m)
    lowerIgr.gridReferencePrecision should be (10000)
  }

  test("should return same grid square if requested precision is lower") {
    val Igr = new IrishGridSquareInfo(knownGridRef_1000m)

    val lowerIgr = Igr.getLowerPrecisionGridSquareInfo(100)

    lowerIgr should be (Igr)
  }

  test("should return same grid square if requested precision is the same") {
    val Igr = new IrishGridSquareInfo(knownGridRef_1000m)

    val lowerIgr = Igr.getLowerPrecisionGridSquareInfo(1000)

    lowerIgr should be (Igr)
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

  test("should give correct grid ref for easting 333634 & northing 373505")
  {
    val igr = IrishGridSquareInfo(333634, 373505)

    igr.gridReference should be ("J336735")
  }

  test("should give correct easting and northing for grid ref J336735")
  {
    val igr = IrishGridSquareInfo("J336735")

    val (easting, northing) = igr.getEastingNorthing

    easting should be (333600)
    northing should be (373500)
  }

  test("should give correct grid ref for easting 316587 & northing 234932")
  {
    val igr = IrishGridSquareInfo(316587, 234932)

    igr.gridReference should be ("O165349")
  }

  test("should give correct easting and northing for grid ref O165349")
  {
    val igr = IrishGridSquareInfo("O165349")

    val (easting, northing) = igr.getEastingNorthing

    easting should be (316500)
    northing should be (234900)
  }

  test("should give correct grid ref for easting 91246 & northing 71413")
  {
    val igr = IrishGridSquareInfo(91246, 71413)

    igr.gridReference should be ("V912714")
  }

  test("should give correct easting and northing for grid ref V912714")
  {
    val igr = IrishGridSquareInfo("V912714")

    val (easting, northing) = igr.getEastingNorthing

    easting should be (91200)
    northing should be (71400)
  }

  test("should give correct grid ref for Lat 60.157057 lng -1.1515654") {
    val igr = IrishGridSquareInfo(53.352140, -6.2505580)
    igr.gridReference should be ("O165349")
  }


}
