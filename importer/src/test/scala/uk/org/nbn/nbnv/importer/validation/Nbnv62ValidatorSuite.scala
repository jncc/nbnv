package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv62ValidatorSuite extends BaseFunSuite{
  test("Nbnv62 should validate null survey key") {
    val record = mock[NbnRecord]
    when(record.surveyKey).thenReturn(null)

    val v = new Nbnv62Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv62 should validate surveykey of 30 chars or less") {
    val record = mock[NbnRecord]
    when(record.surveyKey).thenReturn("a" * 30)

    val v = new Nbnv62Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv62 should not validate survey key > 30 chars") {
    val record = mock[NbnRecord]
    when(record.surveyKey).thenReturn("a" * 50)

    val v = new Nbnv62Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
