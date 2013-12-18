package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}
import java.util.{Date, Calendar}
import java.text.SimpleDateFormat

class Nbnv76ValidatorSuite extends BaseFunSuite with BeforeAndAfter {

  var record: NbnRecord = _
  var v: Nbnv76Validator = _

  before {
    record = mock[NbnRecord]
    when(record.key).thenReturn("1")
    v = new Nbnv76Validator

  }

  test("should record 2 valdiation errors if the start date is defined and the end date is not the end of the year"){
    when(record.startDate).thenReturn("01/01/1997".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/1997"))
    when(record.endDate).thenReturn("30/12/1997".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.count(r => r.level == ResultLevel.ERROR) should be (2)
  }

  test("should validate if the end date supplied is the end of the year") {
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("31/12/1997"))
    when(record.endDate).thenReturn("31/12/1997".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate if a start date is given") {
    when(record.startDate).thenReturn("01/01/1997".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/1997"))
    when(record.endDate).thenReturn("31/12/1997".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate if the end date is not the end of the year") {
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("30/12/1997"))
    when(record.endDate).thenReturn("30/12/1997".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate if the end date supplied is  the end of year in the future") {
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("31/12/9999"))
    when(record.endDate).thenReturn("31/12/9999".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should validate if the end date supplied is the end of the current year") {

    val currentCal = Calendar.getInstance()
    currentCal.setTime(new Date())
    currentCal.set(Calendar.DAY_OF_YEAR, currentCal.getActualMaximum(Calendar.DAY_OF_YEAR))

    val df = new SimpleDateFormat("dd/MM/yyyy");

    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some(df.format(currentCal.getTime))) //satisfies the need for a date string
    when(record.endDate).thenReturn(Some(currentCal.getTime))

    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

}
