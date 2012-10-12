package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.data.Repository
import org.mockito.Mockito._
import org.mockito.Matchers._
import uk.org.nbn.nbnv.importer.ImportFailedException

class GridSquareInfoFactorySuite extends BaseFunSuite{
  test("should recognise irish grid ref") {
    val gridRef = "A166712"
    val repo = mock[Repository]
    val gtm = mock[GridReferenceTypeMapper]

    val fac = new GridSquareInfoFactory(repo, gtm)
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("OSNI")
  }

  test("should recognise irish dinty grid ref") {
    val repo = mock[Repository]
    val gtm = mock[GridReferenceTypeMapper]
    val gridRef = "A17Q"


    val fac = new GridSquareInfoFactory(repo, gtm)
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("OSNI")
  }

  test("should recognise british grid ref") {
    val repo = mock[Repository]
    val gtm = mock[GridReferenceTypeMapper]
    val gridRef = "NN166712"

    val fac = new GridSquareInfoFactory(repo, gtm)
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("OSGB36")
  }
  test("should recognise british dinty grid ref") {
    val repo = mock[Repository]
    val gtm = mock[GridReferenceTypeMapper]
    val gridRef = "NN17Q"

    val fac = new GridSquareInfoFactory(repo, gtm)
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("OSGB36")
  }

  test("should recognise ci grid ref") {
    val repo = mock[Repository]
    val gtm = mock[GridReferenceTypeMapper]
    val gridRef = "WV166712"

    val fac = new GridSquareInfoFactory(repo, gtm)
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("ED50")
  }

  test("should recognise ci dinty grid ref") {
    val repo = mock[Repository]
    val gtm = mock[GridReferenceTypeMapper]
    val gridRef = "WV17Q"

    val fac = new GridSquareInfoFactory(repo, gtm)
    val gri = fac.getByGridRef(gridRef)

    gri.projection should be ("ED50")
  }

  test("should return gridref for correct grid reference type") {
    val repo = mock[Repository]
    val gtm = mock[GridReferenceTypeMapper]
    when(gtm.get("BNG")).thenReturn(Some("OSGB36"))

    val gridRef = "NN166712"
    val gridRefType = "BNG"

    val fac = new GridSquareInfoFactory(repo, gtm)

    val gr = fac.getByGridRef(gridRef, gridRefType)

    gr.gridReference should be ("NN166712")
    gr.projection should be ("OSGB36")
  }

  test("should throw exception for invalid grid reference type") {
    val repo = mock[Repository]
    val gtm = mock[GridReferenceTypeMapper]
    when(gtm.get(anyString())).thenReturn(None)

    val gridRef = "NN166712"
    val gridRefType = "BADTYPE"

    val fac = new GridSquareInfoFactory(repo, gtm)

    intercept[ImportFailedException] {
      fac.getByGridRef(gridRef, gridRefType)
    }
  }

}
