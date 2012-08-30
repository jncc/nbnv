package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.data.Repository


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

  test("Nvnv64 should validate") {
    val repo = mock[Repository]
    when(repo.confirmTaxonVersionKey("some tvk")).thenReturn(true)

    val record = mock[NbnRecord]
    when(record.taxonVersionKey).thenReturn("some tvk")

    val v = new Nbnv64Validator(repo)
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv64 should not validate") {
    val repo = mock[Repository]
    when(repo.confirmTaxonVersionKey("some tvk")).thenReturn(false)

    val record = mock[NbnRecord]
    when(record.taxonVersionKey).thenReturn("some tvk")

    val v = new Nbnv64Validator(repo)
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("Nvnv66 should validate when null") {
    val record = mock[NbnRecord]
    when(record.absenceText).thenReturn(null)

    val v = new Nbnv66Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv66 should validate when absenceText is presence") {
    val record = mock[NbnRecord]
    when(record.absenceText).thenReturn("Presence")

    val v = new Nbnv66Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv66 should validate when absenceText is absence") {
    val record = mock[NbnRecord]
    when(record.absenceText).thenReturn("Absence")

    val v = new Nbnv66Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv66 should not validate when absenceText is not null presence or absence") {
    val record = mock[NbnRecord]
    when(record.absenceText).thenReturn("asdfsdfsdf")

    val v = new Nbnv66Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("Nvnv67 should validate when sensitiveOccurrenceText is null") {
    val record = mock[NbnRecord]
    when(record.sensitiveOccurrenceText).thenReturn(null)

    val v = new Nbnv67Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv67 should validate when sensitiveOccurrenceText is true") {
    val record = mock[NbnRecord]
    when(record.sensitiveOccurrenceText).thenReturn("true")

    val v = new Nbnv67Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv67 should validate when sensitiveOccurrenceText is false") {
    val record = mock[NbnRecord]
    when(record.sensitiveOccurrenceText).thenReturn("false")

    val v = new Nbnv67Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv67 should not validate when sensitiveOccurrenceText is not null true or false") {
    val record = mock[NbnRecord]
    when(record.sensitiveOccurrenceText).thenReturn("fgadg")

    val v = new Nbnv67Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
