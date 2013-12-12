package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import org.scalatest.BeforeAndAfter
import org.junit.BeforeClass
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import java.util.{Date, Calendar}
import java.text.SimpleDateFormat

class Nbnv284ValidatorSuite extends BaseFunSuite with BeforeAndAfter {

  var record : NbnRecord = _
  var v : Nbnv284Validator = _

  before {
    record = mock[NbnRecord]
    v = new Nbnv284Validator
  }

  test("should validate a start date that is the start of the year and no end date") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2012"))
    when(record.startDate).thenReturn("01/01/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should validate a start date that is the start of the year and an end date that is the end of the year") {
      when(record.eventDateRaw).thenReturn(None)
      when(record.startDateRaw).thenReturn(Some("01/01/2012"))
      when(record.startDate).thenReturn("01/01/2012".maybeDate("dd/MM/yyyy"))
      when(record.endDateRaw).thenReturn(Some("31/12/2012"))
      when(record.endDate).thenReturn("31/12/2012".maybeDate("dd/MM/yyyy"))

      val results = v.validate(record)

      results.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate a start date and an end date for differnet years") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2011"))
    when(record.startDate).thenReturn("01/01/2011".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/2012"))
    when(record.endDate).thenReturn("31/12/2012".maybeDate("dd/MM/yyyy"))

    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate a start date that is not the start of the year") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/02/2012"))
    when(record.startDate).thenReturn("01/02/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate an end date that is not the end of the year") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2012"))
    when(record.startDate).thenReturn("01/01/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("30/11/2012"))
    when(record.endDate).thenReturn("30/11/2012".maybeDate("dd/MM/yyyy"))

    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate just and end date") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("31/12/2012"))
    when(record.endDate).thenReturn("31/12/2012".maybeDate("dd/MM/yyyy"))

    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate if the end date supplied is  the end of year in the future") {
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/9999"))
    when(record.startDate).thenReturn("01/01/9999".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/9999"))
    when(record.endDate).thenReturn("31/12/9999".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should validate if the end date supplied is the end of the current year") {

    val currentCal = Calendar.getInstance()
    currentCal.setTime(new Date())

    val df = new SimpleDateFormat("dd/MM/yyyy")

    currentCal.set(Calendar.DAY_OF_YEAR, currentCal.getActualMinimum(Calendar.DAY_OF_YEAR))

    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some(df.format(currentCal.getTime)))
    when(record.startDate).thenReturn(Some(currentCal.getTime))

    currentCal.set(Calendar.DAY_OF_YEAR, currentCal.getActualMaximum(Calendar.DAY_OF_YEAR))

    when(record.endDateRaw).thenReturn(Some(df.format(currentCal.getTime)))
    when(record.endDate).thenReturn(Some(currentCal.getTime))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }



}
