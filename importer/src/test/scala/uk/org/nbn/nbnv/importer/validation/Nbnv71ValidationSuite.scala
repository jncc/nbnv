package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import java.text.SimpleDateFormat
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv71ValidationSuite extends BaseFunSuite {
  val validator = new Nbnv71Validator
  val record = mock[NbnRecord]

  test("Should validate a start date before an end date") {
    when(record.startDateRaw).thenReturn("16/10/2012")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012"))
    when(record.endDateRaw).thenReturn("17/10/2012")
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("17/10/2012"))

    val r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should not validate a start date after an end date") {
    when(record.startDateRaw).thenReturn("17/10/2012")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("17/10/2012"))
    when(record.endDateRaw).thenReturn("16/10/2012")
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012"))

    val r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }

  test("Should validate a start and end date which are the same") {
    when(record.startDateRaw).thenReturn("16/10/2012")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012"))
    when(record.endDateRaw).thenReturn("16/10/2012")
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012"))

    val r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should validate a record with an event date only") {
    when(record.startDateRaw).thenReturn(null)
    when(record.startDate).thenReturn(null)
    when(record.endDateRaw).thenReturn(null)
    when(record.endDate).thenReturn(null)
    when(record.eventDateRaw).thenReturn("16/10/2012")
    when(record.eventDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012"))

    val r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should fail when no dates are available") {
    when(record.startDateRaw).thenReturn(null)
    when(record.startDate).thenReturn(null)
    when(record.endDateRaw).thenReturn(null)
    when(record.endDate).thenReturn(null)
    when(record.eventDateRaw).thenReturn(null)
    when(record.eventDate).thenReturn(null)

    val r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }
}
