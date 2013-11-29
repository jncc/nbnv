package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv196ValidatorSuite extends BaseFunSuite {
  val record = mock[NbnRecord]
  val v = new Nbnv196Validator

  test("should validate a start and end month") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("feb 2013"))
    when(record.startDate).thenReturn("01/02/2013".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("nov 2013"))
    when(record.endDate).thenReturn("30/11/2013".maybeDate("dd/MM/yyyy"))

    var results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate a missing start date"){
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("nov 2013"))
    when(record.endDate).thenReturn("30/11/2013".maybeDate("dd/MM/yyyy"))

    var results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate a missing end date") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("feb 2013"))
    when(record.startDate).thenReturn("01/02/2013".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    var results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate a start date that contains a day") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("02/02/2013"))
    when(record.startDate).thenReturn("02/02/2013".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("nov 2013"))
    when(record.endDate).thenReturn("30/11/2013".maybeDate("dd/MM/yyyy"))

    var results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate an end date that contains a day") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("feb 2013"))
    when(record.startDate).thenReturn("01/02/2013".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("30/11/2013"))
    when(record.endDate).thenReturn("30/11/2013".maybeDate("dd/MM/yyyy"))

    var results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }
}
