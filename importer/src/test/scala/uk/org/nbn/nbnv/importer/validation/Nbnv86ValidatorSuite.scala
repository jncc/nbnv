package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import scala.Some
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv86ValidatorSuite extends BaseFunSuite {

  test("should validate a posititve latitude <=62 and a positive longitude <= 13") {
    val rec = mock[NbnRecord]
    when(rec.east).thenReturn(Some(13.0))
    when(rec.north).thenReturn(Some(62.0))

    val v = new Nbnv86Validator
    val r = v.validate(rec)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should validate a positive latitude >= 48 and a negative longitude >= -14") {
    val rec = mock[NbnRecord]
    when(rec.east).thenReturn(Some(-14.0))
    when(rec.north).thenReturn(Some(48.0))

    val v = new Nbnv86Validator
    val r = v.validate(rec)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate a latitude > 62") {
    val rec = mock[NbnRecord]
    when(rec.east).thenReturn(Some(13.0))
    when(rec.north).thenReturn(Some(62.1))

    val v = new Nbnv86Validator
    val r = v.validate(rec)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate a latitude < 48") {
    val rec = mock[NbnRecord]
    when(rec.east).thenReturn(Some(13.0))
    when(rec.north).thenReturn(Some(47.9999))

    val v = new Nbnv86Validator
    val r = v.validate(rec)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate a longitude > 13") {
    val rec = mock[NbnRecord]
    when(rec.east).thenReturn(Some(13.1))
    when(rec.north).thenReturn(Some(62.0))

    val v = new Nbnv86Validator
    val r = v.validate(rec)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate a longitude < -14") {
    val rec = mock[NbnRecord]
    when(rec.east).thenReturn(Some(-14.1))
    when(rec.north).thenReturn(Some(62.0))

    val v = new Nbnv86Validator
    val r = v.validate(rec)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }
}
