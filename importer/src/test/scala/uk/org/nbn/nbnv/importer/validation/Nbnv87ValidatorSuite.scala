package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv87ValidatorSuite extends BaseFunSuite {
  test("should validate a valid british easthing and northing") {
    val record = mock[NbnRecord]
    when(record.srs).thenReturn(Some(27700))
    when(record.east).thenReturn(Some(408759.0))
    when(record.north).thenReturn(Some(424612.0))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate an invalid british easting and northing") {
    val record = mock[NbnRecord]
    when(record.srs).thenReturn(Some(27700))
    when(record.east).thenReturn(Some(99999408759.0))
    when(record.north).thenReturn(Some(9999999424612.0))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    println(r.message)
    r.level should be (ResultLevel.ERROR)
  }

  test("should validate a valid irish easting and northing") {
    val record = mock[NbnRecord]
    when(record.srs).thenReturn(Some(29903))
    when(record.east).thenReturn(Some(316587.0))
    when(record.north).thenReturn(Some(234932.0))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate an invalid easting and northing") {
    val record = mock[NbnRecord]
    when(record.srs).thenReturn(Some(29903))
    when(record.east).thenReturn(Some(9999999999316587.0))
    when(record.north).thenReturn(Some(99999999234932.0))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("should validate a valid channel islands easting and northing") {
    val record = mock[NbnRecord]
    when(record.srs).thenReturn(Some(23030))
    when(record.east).thenReturn(Some(516600.0))
    when(record.north).thenReturn(Some(5471200.0))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate an invalid channel islands easting and northing") {
    val record = mock[NbnRecord]
    when(record.srs).thenReturn(Some(23030))
    when(record.east).thenReturn(Some(9999999999516600.0))
    when(record.north).thenReturn(Some(999999999995471200.0))

    val v = new Nbnv87Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
