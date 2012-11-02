package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.{GridTypeDef, SrsDef, PointDef, NbnRecord}
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv87ValidatorSuite extends BaseFunSuite {

  def makePoint(east: Double, north: Double, srs: Int)
    = PointDef(east, north, SrsDef(srs), None)

  def makePoint(east: Double, north: Double, grid: String)
    = PointDef(east, north, GridTypeDef(grid), None)

  test("should validate a valid british easthing and northing") {
    val record = mock[NbnRecord]
    when(record.feature).thenReturn(makePoint(408759.0, 424612.0, 27700))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate a valid british easthing and northing with grid code") {
    val record = mock[NbnRecord]
    when(record.feature).thenReturn(makePoint(408759.0, 424612.0, "OSGB"))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate an invalid british easting and northing") {
    val record = mock[NbnRecord]
    when(record.feature).thenReturn(makePoint(99999408759.0, 9999999424612.0, 27700))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    println(r.message)
    r.level should be (ResultLevel.ERROR)
  }

  test("should validate a valid irish easting and northing") {
    val record = mock[NbnRecord]
    when(record.feature).thenReturn(makePoint(316587.0, 234932.0, 29903))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate a valid irish easting and northing with grid code") {
    val record = mock[NbnRecord]
    when(record.feature).thenReturn(makePoint(316587.0, 234932.0, "OSNI"))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate an invalid easting and northing") {
    val record = mock[NbnRecord]
    when(record.feature).thenReturn(makePoint(9999999999316587.0, 99999999234932.0, 29903))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("should validate a valid channel islands easting and northing") {
    val record = mock[NbnRecord]
    when(record.feature).thenReturn(makePoint(516600.0, 5471200.0, 23030))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate a valid channel islands easting and northing with grid code") {
    val record = mock[NbnRecord]
    when(record.feature).thenReturn(makePoint(516600.0, 5471200.0, "ED50"))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate an invalid channel islands easting and northing") {
    val record = mock[NbnRecord]
    when(record.feature).thenReturn(makePoint(9999999999516600.0, 999999999995471200.0, 23030))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
