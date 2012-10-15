package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.testing.{DataAccessLayer, BaseFunSuite}
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

  test("smoke test - should give british grid square for lat 53.718103 lng -1.8684933") {
    //requries a real database for the grid reference system determination
    val db = (new DataAccessLayer).getDatabase
    val fac = new GridSquareInfoFactory(db)

    val gs = fac.getByCoordinate(-1.8684933, 53.718103, 4326).get

    gs.projection should be ("OSGB36")
    gs.gridReference should be ("SE087246")
  }

  test("smoke test - should give irish grid square for lat 54.622978 lng -7.1389159") {
    //requries a real database for the grid reference system determination
    val db = (new DataAccessLayer).getDatabase
    val fac = new GridSquareInfoFactory(db)

    val gs = fac.getByCoordinate(-7.1389159, 54.622978, 4326).get

    gs.projection should be ("OSNI")
    gs.gridReference should be ("H556753")
  }

  test("smoke test - should give channel islands grid square for lat 49.177422 lng -2.183612") {
    //requries a real database for the grid reference system determination
    val db = (new DataAccessLayer).getDatabase
    val fac = new GridSquareInfoFactory(db)

    val gs = fac.getByCoordinate(-2.183612, 49.177422, 4326).get

    gs.projection should be ("ED50")
    gs.gridReference should be ("WV596477")
  }

  test("should give none for grid ref outside of supported grid systems") {
    val db = (new DataAccessLayer).getDatabase
    val fac = new GridSquareInfoFactory(db)

    val nullGS = fac.getByCoordinate(7.410391,  54.438641, 4326)

    nullGS should be (None)
  }
}
