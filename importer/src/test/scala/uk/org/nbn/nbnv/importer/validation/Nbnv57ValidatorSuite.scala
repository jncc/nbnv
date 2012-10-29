package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv57ValidatorSuite extends BaseFunSuite  {

  test("Nbnv57 should validate if the all nbn eventDate field extensions are specified") {
    val record = mock[NbnRecord]
    when(record.startDateRaw).thenReturn(Some("01/01/2012"))
    when(record.endDateRaw).thenReturn(Some("01/01/2012"))
    when(record.dateType).thenReturn("DD")

    val v = new Nbnv57Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv57 should not validate if no eventDate field is specified") {
    val record = mock[NbnRecord]
    when(record.startDateRaw).thenReturn(null)
    when(record.endDateRaw).thenReturn(null)
    when(record.dateType).thenReturn(null)

    val v = new Nbnv57Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
