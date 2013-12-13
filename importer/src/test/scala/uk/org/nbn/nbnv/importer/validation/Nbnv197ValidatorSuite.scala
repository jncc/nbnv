package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import java.util.{Date, Calendar}
import java.text.SimpleDateFormat
import org.scalatest.BeforeAndAfter

class Nbnv197ValidatorSuite extends BaseFunSuite with BeforeAndAfter {
  var record: NbnRecord = _
  var v : Nbnv197Validator = _

  before {
    record = mock[NbnRecord]
    v = new Nbnv197Validator
  }

  test("should validate a start date and end date of the same year") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2011"))
    when(record.startDate).thenReturn("01/01/2011".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/2011"))
    when(record.endDate).thenReturn("31/12/2011".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should validate a start date and end date of different years") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2001"))
    when(record.startDate).thenReturn("01/01/2001".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/2012"))
    when(record.endDate).thenReturn("31/12/2012".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should validate a start date and end date of a leap year"){
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2012"))
    when(record.startDate).thenReturn("01/01/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/2012"))
    when(record.endDate).thenReturn("31/12/2012".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate a start date but no end date"){
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2012"))
    when(record.startDate).thenReturn("01/01/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate an end date but no start date"){
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("31/12/2012"))
    when(record.endDate).thenReturn("31/12/2012".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate a start date that is not the start of the year") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("21/01/2012"))
    when(record.startDate).thenReturn("21/01/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/2012"))
    when(record.endDate).thenReturn("31/12/2012".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate an end date that is not the end of the year") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2012"))
    when(record.startDate).thenReturn("01/01/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("09/12/2012"))
    when(record.endDate).thenReturn("09/12/2012".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate an end date that is after the end of the current year") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2011"))
    when(record.startDate).thenReturn("01/01/2011".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/9999"))
    when(record.endDate).thenReturn("31/12/9999".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should validate an end date that is the end of the current year") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2012"))
    when(record.startDate).thenReturn("01/01/2012".maybeDate("dd/MM/yyyy"))

    val currentCal = Calendar.getInstance()
    currentCal.setTime(new Date())

    val df = new SimpleDateFormat("dd/MM/yyyy")

    currentCal.set(Calendar.DAY_OF_YEAR, currentCal.getActualMaximum(Calendar.DAY_OF_YEAR))

    when(record.endDateRaw).thenReturn(Some(df.format(currentCal.getTime)))
    when(record.endDate).thenReturn(Some(currentCal.getTime))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

}
