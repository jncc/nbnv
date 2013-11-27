package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.records.NbnRecord

class Nbnv817ValidatorSuite extends BaseFunSuite {
  test("should validate record with only date specified") {
    val rec = mock[NbnRecord]
    when(rec.eventDateRaw).thenReturn(Some("01/02/2008"))
    when(rec.startDateRaw).thenReturn(None)
    when(rec.endDateRaw).thenReturn(None)

    val v = new Nbnv817Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate record with only start date specified") {
    val rec = mock[NbnRecord]
    when(rec.eventDateRaw).thenReturn(None)
    when(rec.startDateRaw).thenReturn(Some("01/02/2008"))
    when(rec.endDateRaw).thenReturn(None)

    val v = new Nbnv817Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate record with only end date specified") {
    val rec = mock[NbnRecord]
    when(rec.eventDateRaw).thenReturn(None)
    when(rec.startDateRaw).thenReturn(None)
    when(rec.endDateRaw).thenReturn(Some("01/02/2008"))

    val v = new Nbnv817Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate record with start and end date specified") {
    val rec = mock[NbnRecord]
    when(rec.eventDateRaw).thenReturn(None)
    when(rec.startDateRaw).thenReturn(Some("01/02/2008"))
    when(rec.endDateRaw).thenReturn(Some("01/02/2008"))

    val v = new Nbnv817Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate record with start date and date specified") {
    val rec = mock[NbnRecord]
    when(rec.eventDateRaw).thenReturn(Some("01/02/2008"))
    when(rec.startDateRaw).thenReturn(Some("01/02/2008"))
    when(rec.endDateRaw).thenReturn(None)

    val v = new Nbnv817Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate record with end date and date specified") {
    val rec = mock[NbnRecord]
    when(rec.eventDateRaw).thenReturn(Some("01/02/2008"))
    when(rec.startDateRaw).thenReturn(None)
    when(rec.endDateRaw).thenReturn(Some("01/02/2008"))

    val v = new Nbnv817Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate record with all dates specified") {
    val rec = mock[NbnRecord]
    when(rec.eventDateRaw).thenReturn(Some("01/02/2008"))
    when(rec.startDateRaw).thenReturn(Some("01/02/2008"))
    when(rec.endDateRaw).thenReturn(Some("01/02/2008"))

    val v = new Nbnv817Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.ERROR)
  }
}
