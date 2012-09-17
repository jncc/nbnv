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
    var bgr = new BritishGridSquare(knownGridRef_100m)

    bgr.gridReference should be (knownGridRef_100m)
  }

  test("10m grid ref should be blured to 100m grid") {
    var bgr = new BritishGridSquare(knownGridRef_10m)

    bgr.gridReference should be (knownGridRef_100m)
  }

  test("100m grid ref should have precision = 100") {
    var bgr = new BritishGridSquare(knownGridRef_100m)

    bgr.gridReferencePrecision should be (100)
  }

  test("1000m grid ref should have precision = 1000") {
    var bgr = new BritishGridSquare(knownGridRef_1000m)

    bgr.gridReferencePrecision should be (1000)
  }

  test("2000m DINTY grid ref should have precision = 1000") {
    var bgr = new BritishGridSquare(knownGridRef_2000m)

    bgr.gridReferencePrecision should be (2000)
  }

  test("10000m DINTY grid ref should have precision = 10000") {
    var bgr = new BritishGridSquare(knownGridRef_10000m)

    bgr.gridReferencePrecision should be (10000)
  }
}
