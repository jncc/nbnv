package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel


class ValidationSuite extends BaseFunSuite {
  //surveyKey < 30 chars
  test("Nvnv62 should validate") {
    val record = mock[NbnRecord]
    when(record.surveyKey).thenReturn("a" * 30)

    val v = new Nbnv62Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv62 should not validate") {
    val record = mock[NbnRecord]
    when(record.surveyKey).thenReturn("a" * 50)

    val v = new Nbnv62Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("Nvnv63 should validate") {
    val record = mock[NbnRecord]
    when(record.sampleKey).thenReturn("a" * 30)

    val v = new Nbnv63Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv63 should not validate") {
    val record = mock[NbnRecord]
    when(record.sampleKey).thenReturn("a" * 50)

    val v = new Nbnv63Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
