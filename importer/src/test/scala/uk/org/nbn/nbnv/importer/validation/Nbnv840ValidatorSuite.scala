package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.scalatest.BeforeAndAfter

class Nbnv840ValidatorSuite extends BaseFunSuite with BeforeAndAfter {
  var record :NbnRecord = _
  var v : Nbnv840Validator = _

  before {
    record = mock[NbnRecord]
    v = new Nbnv840Validator
  }

  test("should validate a record that does not have an event date defined") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.dateType).thenReturn(Some("D"))

    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate a record that has an event date and a date type of 'D") {
    when(record.eventDateRaw).thenReturn(Some("01/02/2012"))
    when(record.dateType).thenReturn(Some("D"))

    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate a record that has an event date and a date type that is not 'D'") {
    when(record.eventDateRaw).thenReturn(Some("01/02/2012"))
    when(record.dateType).thenReturn(Some("DD"))

    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("should validate a record that has an event date and no date type") {
    when(record.eventDateRaw).thenReturn(Some("01/02/2012"))
    when(record.dateType).thenReturn(None)

    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }
}
