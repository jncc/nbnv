package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite

class GridReferenceTypeMapperSuite extends BaseFunSuite {
  test("should map BNG to OSGB") {
    val gtm = new GridReferenceTypeMapper
    val gt = gtm.get("BNG")

    gt should be (Option("OSGB36"))
  }

  test("should return None for unknown grid reference type") {
    val gtm = new GridReferenceTypeMapper
    val gt = gtm.get("BINGO bongo")

    gt should be (None)
  }
}
