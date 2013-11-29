package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import java.text.SimpleDateFormat
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import org.scalatest.BeforeAndAfter

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 */
class Nbnv74ValidatorSuite extends BaseFunSuite with BeforeAndAfter {


  val validator = new Nbnv74Validator
  var record: NbnRecord = _

  before {
    record = mock[NbnRecord]
  }

  test("Should validate a valid O type date set for a leap year") {
    val startDateString = "01/02/2004"
    val endDateString = "29/02/2004"

    when(record.dateType).thenReturn("O")
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

    when(record.dateType).thenReturn("O")
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

    when(record.dateType).thenReturn("O")
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

    when(record.dateType).thenReturn("O")
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

    when(record.dateType).thenReturn("O")
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Option(startDateString))
    when(record.startDate).thenReturn(Option( new SimpleDateFormat("dd/MM/yyyy").parse(startDateString)))
    when(record.endDateRaw).thenReturn(Option(endDateString))
    when(record.endDate).thenReturn(Option(new SimpleDateFormat("dd/MM/yyyy").parse(endDateString)))

    val r = validator.validate(record)
    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

}
