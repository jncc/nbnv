package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import java.text.SimpleDateFormat
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import java.util.{Date, Calendar}

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 */
class Nbnv74ValidatorSuite extends BaseFunSuite with BeforeAndAfter {

  var validator : Nbnv74Validator = _
  var record: NbnRecord = _

  before {
    record = mock[NbnRecord]
    validator = new Nbnv74Validator
  }

  test("Should validate a valid O type date set for a leap year") {
    val startDateString = "01/02/2004"
    val endDateString = "29/02/2004"

    when(record.dateType).thenReturn(Some("O"))
    when(record.startDateRaw).thenReturn(Option(startDateString))
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(Option( new SimpleDateFormat("dd/MM/yyyy").parse(startDateString)))
    when(record.endDateRaw).thenReturn(Option(endDateString))
    when(record.endDate).thenReturn(Option(new SimpleDateFormat("dd/MM/yyyy").parse(endDateString)))

    val r = validator.validate(record)
    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("Should validate a valid O type date set for a normal year") {
    val startDateString = "01/02/2005"
    val endDateString = "28/02/2005"

    when(record.dateType).thenReturn(Some("O"))
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Option(startDateString))
    when(record.startDate).thenReturn(Option( new SimpleDateFormat("dd/MM/yyyy").parse(startDateString)))
    when(record.endDateRaw).thenReturn(Option(endDateString))
    when(record.endDate).thenReturn(Option(new SimpleDateFormat("dd/MM/yyyy").parse(endDateString)))

    val r = validator.validate(record)
    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("Should not validate a start date that is not the start of the month") {
    val startDateString = "02/02/2005"
    val endDateString = "28/02/2005"

    when(record.dateType).thenReturn(Some("O"))
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Option(startDateString))
    when(record.startDate).thenReturn(Option( new SimpleDateFormat("dd/MM/yyyy").parse(startDateString)))
    when(record.endDateRaw).thenReturn(Option(endDateString))
    when(record.endDate).thenReturn(Option(new SimpleDateFormat("dd/MM/yyyy").parse(endDateString)))

    val r = validator.validate(record)
    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate an end date that is not the end of the month") {
    val startDateString = "01/02/2005"
    val endDateString = "24/02/2005"

    when(record.dateType).thenReturn(Some("O"))
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Option(startDateString))
    when(record.startDate).thenReturn(Option( new SimpleDateFormat("dd/MM/yyyy").parse(startDateString)))
    when(record.endDateRaw).thenReturn(Option(endDateString))
    when(record.endDate).thenReturn(Option(new SimpleDateFormat("dd/MM/yyyy").parse(endDateString)))

    val r = validator.validate(record)
    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate an end date for a different month") {
    val startDateString = "01/02/2005"
    val endDateString = "31/03/2005"

    when(record.dateType).thenReturn(Some("O"))
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Option(startDateString))
    when(record.startDate).thenReturn(Option( new SimpleDateFormat("dd/MM/yyyy").parse(startDateString)))
    when(record.endDateRaw).thenReturn(Option(endDateString))
    when(record.endDate).thenReturn(Option(new SimpleDateFormat("dd/MM/yyyy").parse(endDateString)))

    val r = validator.validate(record)
    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate an end date that is beyond the end of the current year") {
    val startDateString = "01/02/9999"
    val endDateString = "28/02/9999"

    when(record.dateType).thenReturn(Some("O"))
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Option(startDateString))
    when(record.startDate).thenReturn(startDateString.maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Option(endDateString))
    when(record.endDate).thenReturn(endDateString.maybeDate("dd/MM/yyyy"))

    val r = validator.validate(record)
    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should validate and end date that is the end of the current year") {
    val currentCal = Calendar.getInstance()
    currentCal.setTime(new Date())

    val df = new SimpleDateFormat("dd/MM/yyyy")

    currentCal.set(Calendar.DAY_OF_YEAR, currentCal.getActualMaximum(Calendar.DAY_OF_YEAR))

    when(record.endDateRaw).thenReturn(Some(df.format(currentCal.getTime)))
    when(record.endDate).thenReturn(Some(currentCal.getTime))

    currentCal.set(Calendar.DAY_OF_MONTH, currentCal.getActualMinimum(Calendar.DAY_OF_MONTH))

    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some(df.format(currentCal.getTime)))
    when(record.startDate).thenReturn(Some(currentCal.getTime))

    val r = validator.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

}
