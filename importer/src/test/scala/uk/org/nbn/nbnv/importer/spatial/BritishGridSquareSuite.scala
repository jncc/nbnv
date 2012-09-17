package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._

class BritishGridSquareSuite extends BaseFunSuite {
  val knownGridRef_10m = "NN16637123"
  val knownGridRef_100m = "NN166712"
  val knownGridRef_1000m = "NN1671"
  val knownGridRef_2000m = "NN17Q"
  val knownGridRef_10000m = "NN17"

  test("should output an unblurred grid referce") {
    val bgr = new BritishGridSquare(knownGridRef_100m)

    bgr.gridReference should be (knownGridRef_100m)
  }

  test("10m grid ref should be blured to 100m grid") {
    val bgr = new BritishGridSquare(knownGridRef_10m)

    bgr.gridReference should be (knownGridRef_100m)
  }

  test("100m grid ref should have precision = 100") {
    val bgr = new BritishGridSquare(knownGridRef_100m)

    bgr.gridReferencePrecision should be (100)
  }

  test("1000m grid ref should have precision = 1000") {
    val bgr = new BritishGridSquare(knownGridRef_1000m)

    bgr.gridReferencePrecision should be (1000)
  }

  test("2000m DINTY grid ref should have precision = 2000") {
    val bgr = new BritishGridSquare(knownGridRef_2000m)

    bgr.gridReferencePrecision should be (2000)
  }

  test("10000m DINTY grid ref should have precision = 10000") {
    val bgr = new BritishGridSquare(knownGridRef_10000m)

    bgr.gridReferencePrecision should be (10000)
  }

  test("should blur 100m grid ref to 1000m") {
    val bgr = new BritishGridSquare(knownGridRef_100m, 1000)

    bgr.gridReference should be (knownGridRef_1000m)
    bgr.gridReferencePrecision should be(1000)
  }

  test("should blur 100m grid ref to 2000m") {
    val bgr = new BritishGridSquare(knownGridRef_100m, 2000)

    bgr.gridReference should be (knownGridRef_2000m)
    bgr.gridReferencePrecision should be (2000)
  }

  test("should blur 100m grid ref to 10000m") {
    val bgr = new BritishGridSquare(knownGridRef_100m, 10000)

    bgr.gridReference should be (knownGridRef_10000m)
    bgr.gridReferencePrecision should be (10000)
  }

  test("should blur DINTY grid ref to 10000m") {
    val bgr = new BritishGridSquare(knownGridRef_2000m, 10000)

    bgr.gridReference should be (knownGridRef_10000m)
    bgr.gridReferencePrecision should be (10000)
  }

  test("should normailise precision of 150 to 1000m") {
    val bgr = new BritishGridSquare(knownGridRef_100m, 150)

    bgr.gridReferencePrecision should be (1000)
    bgr.gridReference should be (knownGridRef_1000m)
  }

  test("should give WKT for 100m grid square") {
    val bgr = new BritishGridSquare(knownGridRef_100m)

    bgr.wgs84Polygon should be
    ("POLYGON((-5.0047134199132 56.796095877734665, " +
      "-5.005431742013905 56.805067024540264, " +
      "-4.989078600976499 56.80546011145297, " +
      "-4.988364172840809 56.79648883108535, " +
      "-5.0047134199132 56.796095877734665))")
  }

}
