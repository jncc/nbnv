package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite

class GridSquareInfoFactorySuite extends BaseFunSuite{
  test("should recognise irish grid ref") {
    val gridRef = "A166712"

    val fac = new GridSquareInfoFactory()
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("OSNI")
  }

  test("should recognise irish dinty grid ref") {
    val gridRef = "A17Q"

    val fac = new GridSquareInfoFactory()
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("OSNI")
  }

  test("should recognise british grid ref") {
    val gridRef = "NN166712"

    val fac = new GridSquareInfoFactory()
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("OSGB36")
  }
  test("should recognise british dinty grid ref") {
    val gridRef = "NN17Q"

    val fac = new GridSquareInfoFactory()
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("OSGB36")
  }

  test("should recognise ci grid ref") {
    val gridRef = "WV166712"

    val fac = new GridSquareInfoFactory()
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("ED50")
  }

  test("should recognise ci dinty grid ref") {
    val gridRef = "WV17Q"

    val fac = new GridSquareInfoFactory()
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("ED50")
  }

}
