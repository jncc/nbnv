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

  test("10m grid should be blured to 100m grid") {
    var bgr = new BritishGridSquare(knownGridRef_10m)

    bgr.gridReference should be (knownGridRef_100m)
  }
}
