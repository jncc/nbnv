package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import java.text.SimpleDateFormat
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv72ValidatorSuite extends BaseFunSuite {
  val validator = new Nbnv72Validator
  val record = mock[NbnRecord]

  test("Should validate a start date before an end date") {
    when(record.startDateRaw).thenReturn(Some("16/10/2012"))
    when(record.startDate).thenReturn(Some(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012")))
    when(record.endDateRaw).thenReturn(Some("17/10/2012"))
    when(record.endDate).thenReturn(Some(new SimpleDateFormat("dd/MM/yyyy").parse("17/10/2012")))

    val r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should not validate a start date after an end date") {
    when(record.startDateRaw).thenReturn(Some("17/10/2012"))
    when(record.startDate).thenReturn(Some(new SimpleDateFormat("dd/MM/yyyy").parse("17/10/2012")))
    when(record.endDateRaw).thenReturn(Some("16/10/2012"))
    when(record.endDate).thenReturn(Some(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012")))

    val r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }

  test("Should validate a start and end date which are the same") {
    when(record.startDateRaw).thenReturn(Some("16/10/2012"))
    when(record.startDate).thenReturn(Some(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012")))
    when(record.endDateRaw).thenReturn(Some("16/10/2012"))
    when(record.endDate).thenReturn(Some(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012")))

    val r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should skip rule when no dates are available") {
    when(record.startDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)


    val r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }
}
