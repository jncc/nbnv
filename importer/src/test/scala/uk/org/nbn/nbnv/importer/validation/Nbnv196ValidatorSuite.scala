package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import org.scalatest.BeforeAndAfter

class Nbnv196ValidatorSuite extends BaseFunSuite with BeforeAndAfter {
  var record : NbnRecord = _
  var v : Nbnv196Validator = _

  before {
    record = mock[NbnRecord]
    v = new Nbnv196Validator
  }


  test("should validate a start date that is the start of the month and an end date that is the end of the month") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/02/2013"))
    when(record.startDate).thenReturn("01/02/2013".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("30/11/2013"))
    when(record.endDate).thenReturn("30/11/2013".maybeDate("dd/MM/yyyy"))

    var results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate a missing start date"){
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("30/11/2013"))
    when(record.endDate).thenReturn("30/11/2013".maybeDate("dd/MM/yyyy"))

    var results = v.validate(record)

    val errors = results.find(r => r.level == ResultLevel.ERROR)
    errors  should not be ('empty)
    errors.count(x => true) should be (1)
  }

  test("should not validate a missing end date") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/02/2013"))
    when(record.startDate).thenReturn("01/02/2013".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    var results = v.validate(record)

    val errors = results.find(r => r.level == ResultLevel.ERROR)
    errors  should not be ('empty)
    errors.count(x => true) should be (1)
  }

  test("should not validate a start date that is not the start of the month") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("02/02/2013"))
    when(record.startDate).thenReturn("02/02/2013".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("30/11/2013"))
    when(record.endDate).thenReturn("30/11/2013".maybeDate("dd/MM/yyyy"))

    var results = v.validate(record)

    val errors = results.find(r => r.level == ResultLevel.ERROR)
    errors  should not be ('empty)
    errors.count(x => true) should be (1)
  }

  test("should not validate an end date that is not the end of the month") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/02/2013"))
    when(record.startDate).thenReturn("01/02/2013".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("29/11/2013"))
    when(record.endDate).thenReturn("29/11/2013".maybeDate("dd/MM/yyyy"))

    var results = v.validate(record)

    val errors = results.find(r => r.level == ResultLevel.ERROR)
    errors  should not be ('empty)
    errors.count(x => true) should be (1)
  }

  test("should not validate an end date that is in the same month and year as the start date") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/02/2012"))
    when(record.startDate).thenReturn("01/02/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("29/02/2012"))
    when(record.endDate).thenReturn("29/02/2012".maybeDate("dd/MM/yyyy"))

    var results = v.validate(record)

    val errors = results.find(r => r.level == ResultLevel.ERROR)
    errors  should not be ('empty)
    errors.count(x => true) should be (1)
  }

  test("should validate an end date that is in the same month and a different year as the start date") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/02/2012"))
    when(record.startDate).thenReturn("01/02/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("28/02/2013"))
    when(record.endDate).thenReturn("28/02/2013".maybeDate("dd/MM/yyyy"))

    var results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }
}
