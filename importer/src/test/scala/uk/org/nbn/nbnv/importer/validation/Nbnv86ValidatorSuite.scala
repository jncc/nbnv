package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import scala.Some
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv86ValidatorSuite extends BaseFunSuite {

  test("should validate a posititve latitude <= 90 and a positive longitude <= 180") {
    val rec = mock[NbnRecord]
    when(rec.east).thenReturn(Some(180.0))
    when(rec.north).thenReturn(Some(90.0))

    val v = new Nbnv86Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate a negative latitude >= -90 and a positive longitude >= -180") {
    val rec = mock[NbnRecord]
    when(rec.east).thenReturn(Some(-180.0))
    when(rec.north).thenReturn(Some(-90.0))

    val v = new Nbnv86Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate a latitude > 90") {
    val rec = mock[NbnRecord]
    when(rec.east).thenReturn(Some(180.0))
    when(rec.north).thenReturn(Some(95.0))

    val v = new Nbnv86Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate a latitude < -90") {
    val rec = mock[NbnRecord]
    when(rec.east).thenReturn(Some(180.0))
    when(rec.north).thenReturn(Some(-95.0))

    val v = new Nbnv86Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate a longitude > 180") {
    val rec = mock[NbnRecord]
    when(rec.east).thenReturn(Some(185.0))
    when(rec.north).thenReturn(Some(90.0))

    val v = new Nbnv86Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate a longitude < -180") {
    val rec = mock[NbnRecord]
    when(rec.east).thenReturn(Some(-185.0))
    when(rec.north).thenReturn(Some(90.0))

    val v = new Nbnv86Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.ERROR)
  }
}
