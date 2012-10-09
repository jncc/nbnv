package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._

class BritishGridSquareInfoSuite extends BaseFunSuite {
  val knownGridRef_1m = "NN1663471237"
  val knownGridRef_10m = "NN16637123"
  val knownGridRef_100m = "NN166712"
  val knownGridRef_1000m = "NN1671"
  val knownGridRef_2000m = "NN17Q"
  val knownGridRef_10000m = "NN17"

  test("should identify projection as OSGB36") {
    val bgr = new BritishGridSquareInfo(knownGridRef_100m)

    bgr.projection should be ("OSGB36")
  }

  test("should identify EPSG code as 27700") {
    val bgr = new BritishGridSquareInfo(knownGridRef_100m)

    bgr.epsgCode should be ("27700")
  }

  test("should output an unblurred grid referce") {
    val bgr = new BritishGridSquareInfo(knownGridRef_100m)

    bgr.gridReference should be (knownGridRef_100m)
  }

  test("1m grid ref should be blured to 100m grid automatically") {
    val bgr = new BritishGridSquareInfo(knownGridRef_1m)

    bgr.gridReference should be (knownGridRef_100m)
    bgr.gridReferencePrecision should be (100)
  }

  test("10m grid ref should be blured to 100m grid automatically") {
    val bgr = new BritishGridSquareInfo(knownGridRef_10m)

    bgr.gridReference should be (knownGridRef_100m)
    bgr.gridReferencePrecision should be (100)
  }

  test("100m grid ref should have precision = 100") {
    val bgr = new BritishGridSquareInfo(knownGridRef_100m)

    bgr.gridReferencePrecision should be (100)
  }

  test("1000m grid ref should have precision = 1000") {
    val bgr = new BritishGridSquareInfo(knownGridRef_1000m)

    bgr.gridReferencePrecision should be (1000)
  }

  test("2000m DINTY grid ref should have precision = 2000") {
    val bgr = new BritishGridSquareInfo(knownGridRef_2000m)

    bgr.gridReferencePrecision should be (2000)
  }

  test("10000m grid ref should have precision = 10000") {
    val bgr = new BritishGridSquareInfo(knownGridRef_10000m)

    bgr.gridReferencePrecision should be (10000)
  }

  test("should blur 100m grid ref to 1000m") {
    val bgr = new BritishGridSquareInfo(knownGridRef_100m, 1000)

    bgr.gridReference should be (knownGridRef_1000m)
    bgr.gridReferencePrecision should be(1000)
  }

  test("should blur 100m grid ref to 2000m") {
    val bgr = new BritishGridSquareInfo(knownGridRef_100m, 2000)

    bgr.gridReference should be (knownGridRef_2000m)
    bgr.gridReferencePrecision should be (2000)
  }

  test("should blur 100m grid ref to 10000m") {
    val bgr = new BritishGridSquareInfo(knownGridRef_100m, 10000)

    bgr.gridReference should be (knownGridRef_10000m)
    bgr.gridReferencePrecision should be (10000)
  }

  test("should blur DINTY grid ref to 10000m") {
    val bgr = new BritishGridSquareInfo(knownGridRef_2000m, 10000)

    bgr.gridReference should be (knownGridRef_10000m)
    bgr.gridReferencePrecision should be (10000)
  }

  test("should normailise precision of 30 to 100m") {
    val bgr = new BritishGridSquareInfo(knownGridRef_100m, 30)

    bgr.gridReferencePrecision should be (100)
    bgr.gridReference should be (knownGridRef_100m)
  }

  test("should normailise precision of 150 to 1000m") {
    val bgr = new BritishGridSquareInfo(knownGridRef_100m, 150)

    bgr.gridReferencePrecision should be (1000)
    bgr.gridReference should be (knownGridRef_1000m)
  }

  test("should normailise precision of 1200 to 2000m") {
    val bgr = new BritishGridSquareInfo(knownGridRef_1000m, 1200)

    bgr.gridReferencePrecision should be (2000)
    bgr.gridReference should be (knownGridRef_2000m)
  }

  test("should normailise precision of 8000 to 10000m") {
    val bgr = new BritishGridSquareInfo(knownGridRef_2000m, 8000)

    bgr.gridReferencePrecision should be (10000)
    bgr.gridReference should be (knownGridRef_10000m)
  }

  test("should give WKT for 100m grid square in WGS84") {
    val bgr = new BritishGridSquareInfo(knownGridRef_100m)

    bgr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 1000m grid square in WGS84") {
    val bgr = new BritishGridSquareInfo(knownGridRef_1000m)

    bgr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 2000m grid square in WGS84") {
    val bgr = new BritishGridSquareInfo(knownGridRef_2000m)

    bgr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 10000m grid square in WGS84") {
    val bgr = new BritishGridSquareInfo(knownGridRef_10000m)

    bgr.wgs84Polygon matches (TestResources.polygonWKTRegex)
  }

  test("should give 1000m grid square as parent of 100m") {
    val bgr = new BritishGridSquareInfo(knownGridRef_100m)

    bgr.getParentGridSquareInfo match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_1000m)
        parent.gridReferencePrecision should be (1000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should give 2000m grid square as parent of 1000m") {
    val bgr = new BritishGridSquareInfo(knownGridRef_1000m)

    bgr.getParentGridSquareInfo match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_2000m)
        parent.gridReferencePrecision should be (2000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should give 10000m grid square as parent of 2000m") {
    val bgr = new BritishGridSquareInfo(knownGridRef_2000m)

    bgr.getParentGridSquareInfo match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_10000m)
        parent.gridReferencePrecision should be (10000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should be no parent of 10000m grid square") {
    val bgr = new BritishGridSquareInfo(knownGridRef_10000m)

    bgr.getParentGridSquareInfo should be (None)
  }

  test("should compute 2000m grid ref from 100m grid ref") {
    val bgr = new BritishGridSquareInfo(knownGridRef_100m)

    val lowerBgr = bgr.getLowerPrecisionGridSquareInfo(2000)

    lowerBgr should not be (null)
    lowerBgr.gridReference should be (knownGridRef_2000m)
    lowerBgr.gridReferencePrecision should be (2000)
  }

  test("should compute 10000m grid ref from 1000m grid ref") {
    val bgr = new BritishGridSquareInfo(knownGridRef_1000m)

    val lowerBgr = bgr.getLowerPrecisionGridSquareInfo(10000)

    lowerBgr should not be (null)
    lowerBgr.gridReference should be (knownGridRef_10000m)
    lowerBgr.gridReferencePrecision should be (10000)
  }

  test("should return same grid square if requested precision is lower") {
    val bgr = new BritishGridSquareInfo(knownGridRef_1000m)

    val lowerBgr = bgr.getLowerPrecisionGridSquareInfo(100)

    lowerBgr should be (bgr)
  }

  test("should return same grid square if requested precision is the same") {
    val bgr = new BritishGridSquareInfo(knownGridRef_1000m)

    val lowerBgr = bgr.getLowerPrecisionGridSquareInfo(1000)

    lowerBgr should be (bgr)
  }

  test("should give WKT for 100m grid square") {
    val bgr = new BritishGridSquareInfo(knownGridRef_100m)

    bgr.sourceProjectionPolygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 1000m grid square") {
    val bgr = new BritishGridSquareInfo(knownGridRef_1000m)

    bgr.sourceProjectionPolygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 2000m grid square") {
    val bgr = new BritishGridSquareInfo(knownGridRef_2000m)

    bgr.sourceProjectionPolygon matches (TestResources.polygonWKTRegex)
  }

  test("should give WKT for 10000m grid square") {
    val bgr = new BritishGridSquareInfo(knownGridRef_10000m)

    bgr.sourceProjectionPolygon matches (TestResources.polygonWKTRegex)
  }

  test("should give correct easting and northing for grid ref TQ24") {
    val bgr = BritishGridSquareInfo("TQ24")
    val (easting, northing) = bgr.getEastingNorthing

    easting should be (520000)
    northing should be (140000)
  }

  test("should give correct grid ref for easting 408759 & norhting 424612")
  {
    val bgr = BritishGridSquareInfo(408759, 424612)

    bgr.gridReference should be ("SE087246")
  }

  test("should give correct easting northing for grid ref SE087246")
  {
    val bgr = BritishGridSquareInfo("SE087246")
    val (easting, northing) = bgr.getEastingNorthing

    easting should be (408700)
    northing should be (424600)
  }

  test("should give correct grid ref for easting 520814 & norhting 296511")
  {
    val bgr = BritishGridSquareInfo(520814, 296511)

    bgr.gridReference should be ("TL208965")
  }

  test("should give correct easting northing for grid ref TL208965")
  {
    val bgr = BritishGridSquareInfo("TL208965")
    val (easting, northing) = bgr.getEastingNorthing

    easting should be (520800)
    northing should be (296500)
  }

  test("should give correct grid ref for easting 259207 & norhting 665548")
  {
    val bgr = BritishGridSquareInfo(259207, 665548)

    bgr.gridReference should be ("NS592655")
  }

  test("should give correct easting northing for grid ref NS592655")
  {
    val bgr = BritishGridSquareInfo("NS592655")
    val (easting, northing) = bgr.getEastingNorthing

    easting should be (259200)
    northing should be (665500)
  }

  test("should give correct grid ref for easting 447275 & norhting 1141792")
  {
    val bgr = BritishGridSquareInfo(447275, 1141792)

    bgr.gridReference should be ("HU472417")
  }

  test("should give correct easting northing for grid ref HU472417")
  {
    val bgr = BritishGridSquareInfo("HU472417")
    val (easting, northing) = bgr.getEastingNorthing

    easting should be (447200)
    northing should be (1141700)
  }
}
