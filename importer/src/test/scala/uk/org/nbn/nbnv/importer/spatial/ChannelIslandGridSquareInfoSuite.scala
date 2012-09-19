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
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_100m)

    bgr.projection should be ("ED50")
  }

  test("should output an unblurred grid referce") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_100m)

    bgr.gridReference should be (knownGridRef_100m)
  }

  test("1m grid ref should be blured to 100m grid automatically") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_1m)

    bgr.gridReference should be (knownGridRef_100m)
    bgr.gridReferencePrecision should be (100)
  }

  test("10m grid ref should be blured to 100m grid automatically") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_10m)

    bgr.gridReference should be (knownGridRef_100m)
    bgr.gridReferencePrecision should be (100)
  }

  test("100m grid ref should have precision = 100") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_100m)

    bgr.gridReferencePrecision should be (100)
  }

  test("1000m grid ref should have precision = 1000") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_1000m)

    bgr.gridReferencePrecision should be (1000)
  }

  test("2000m DINTY grid ref should have precision = 2000") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_2000m)

    bgr.gridReferencePrecision should be (2000)
  }

  test("10000m grid ref should have precision = 10000") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_10000m)

    bgr.gridReferencePrecision should be (10000)
  }

  test("should blur 100m grid ref to 1000m") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_100m, 1000)

    bgr.gridReference should be (knownGridRef_1000m)
    bgr.gridReferencePrecision should be(1000)
  }

  test("should blur 100m grid ref to 2000m") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_100m, 2000)

    bgr.gridReference should be (knownGridRef_2000m)
    bgr.gridReferencePrecision should be (2000)
  }

  test("should blur 100m grid ref to 10000m") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_100m, 10000)

    bgr.gridReference should be (knownGridRef_10000m)
    bgr.gridReferencePrecision should be (10000)
  }

  test("should blur DINTY grid ref to 10000m") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_2000m, 10000)

    bgr.gridReference should be (knownGridRef_10000m)
    bgr.gridReferencePrecision should be (10000)
  }

  test("should normailise precision of 30 to 100m") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_100m, 30)

    bgr.gridReferencePrecision should be (100)
    bgr.gridReference should be (knownGridRef_100m)
  }

  test("should normailise precision of 150 to 1000m") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_100m, 150)

    bgr.gridReferencePrecision should be (1000)
    bgr.gridReference should be (knownGridRef_1000m)
  }

  test("should normailise precision of 1200 to 2000m") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_1000m, 1200)

    bgr.gridReferencePrecision should be (2000)
    bgr.gridReference should be (knownGridRef_2000m)
  }

  test("should normailise precision of 8000 to 10000m") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_2000m, 8000)

    bgr.gridReferencePrecision should be (10000)
    bgr.gridReference should be (knownGridRef_10000m)
  }

  test("should give 1000m grid square as parent of 100m") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_100m)

    bgr.getParentGridRef match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_1000m)
        parent.gridReferencePrecision should be (1000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should give 2000m grid square as parent of 1000m") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_1000m)

    bgr.getParentGridRef match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_2000m)
        parent.gridReferencePrecision should be (2000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should give 10000m grid square as parent of 2000m") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_2000m)

    bgr.getParentGridRef match {
      case Some(parent) => {
        parent.gridReference should be (knownGridRef_10000m)
        parent.gridReferencePrecision should be (10000)
      }
      case None => fail("no parent grid reference")
    }
  }

  test("should be no parent of 10000m grid square") {
    val bgr = new ChannelIslandGridSquareInfo(knownGridRef_10000m)

    bgr.getParentGridRef should be (None)
  }

  //todo: test wsg84polygon output
}
