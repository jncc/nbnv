package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import java.text.SimpleDateFormat
import java.util.Date

class Nbnv195ValidatorSuite extends BaseFunSuite with BeforeAndAfter {
  var record : NbnRecord = _
  var v : Nbnv195Validator = _

  before {
    record = mock[NbnRecord]
    v = new Nbnv195Validator
    when(record.dateType).thenReturn(Some("DD"))
  }

  test("should validate a record with a valid start and end date") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("12/01/2012"))
    when(record.startDate).thenReturn("12/01/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("23/09/2013"))
    when(record.endDate).thenReturn("23/09/2013".maybeDate("dd/MM/yyyy"))

    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate a record with no start date") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("23/09/2013"))
    when(record.endDate).thenReturn("23/09/2013".maybeDate("dd/MM/yyyy"))

    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate a record with no end date") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("12/01/2012"))
    when(record.startDate).thenReturn("12/01/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate a record with an end date in the future") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("12/01/2012"))
    when(record.startDate).thenReturn("12/01/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("23/09/9999"))
    when(record.endDate).thenReturn("23/09/9999".maybeDate("dd/MM/yyyy"))

    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should validate a record with an end date of today") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("12/01/2012"))
    when(record.startDate).thenReturn("12/01/2012".maybeDate("dd/MM/yyyy"))

    val df = new SimpleDateFormat("dd/MM/yyyy")
    val now = new Date()

    when(record.endDateRaw).thenReturn(Some(df.format(now)))
    when(record.endDate).thenReturn(Some(now))

    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

}
