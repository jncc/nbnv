package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import org.mockito.Mockito._
import org.mockito.Matchers._
import uk.org.nbn.nbnv.importer.ImportFailedException

class GridSquareInfoFactorySuite extends BaseFunSuite{
  test("should recognise irish grid ref") {
    val gridRef = "A166712"
    val db = mock[Database]

    val fac = new GridSquareInfoFactory(db)
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("OSNI")
  }

  test("should recognise irish dinty grid ref") {

    val gridRef = "A17Q"
    val db = mock[Database]

    val fac = new GridSquareInfoFactory(db)
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("OSNI")
  }

  test("should recognise british grid ref") {
    val gridRef = "NN166712"
    val db = mock[Database]

    val fac = new GridSquareInfoFactory(db)
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("OSGB36")
  }
  test("should recognise british dinty grid ref") {
    val gridRef = "NN17Q"
    val db = mock[Database]

    val fac = new GridSquareInfoFactory(db)
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("OSGB36")
  }

  test("should recognise ci grid ref") {

    val gridRef = "WV166712"
    val db = mock[Database]

    val fac = new GridSquareInfoFactory(db)
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("ED50")
  }

  test("should recognise ci dinty grid ref") {

    val gridRef = "WV17Q"
    val db = mock[Database]

    val fac = new GridSquareInfoFactory(db)
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("ED50")
  }

  test("should return gridref for correct grid reference type") {

    val gridRef = "NN166712"
    val gridRefType = "BNG"
    val db = mock[Database]
    val fac = new GridSquareInfoFactory(db)

    val gr = fac.getByGridRef(gridRef, gridRefType)

    gr.gridReference should be ("NN166712")
    gr.projection should be ("OSGB36")
  }

  test("should throw exception for invalid grid reference type") {
    val db = mock[Database]

    val gridRef = "NN166712"
    val gridRefType = "BADTYPE"

    val fac = new GridSquareInfoFactory(db)

    intercept[ImportFailedException] {
      fac.getByGridRef(gridRef, gridRefType)
    }
  }

}
