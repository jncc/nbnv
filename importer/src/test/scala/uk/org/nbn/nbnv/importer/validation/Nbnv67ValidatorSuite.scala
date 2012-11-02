package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv67ValidatorSuite extends BaseFunSuite{
  test("Nvnv67 should validate when sensitiveOccurrenceRaw is null") {
    val record = mock[NbnRecord]
    when(record.sensitiveOccurrenceRaw).thenReturn(None)

    val v = new Nbnv67Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv67 should validate when sensitiveOccurrenceRaw is true") {
    val record = mock[NbnRecord]
    when(record.sensitiveOccurrenceRaw).thenReturn(Some("true"))

    val v = new Nbnv67Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv67 should validate when sensitiveOccurrenceRaw is false") {
    val record = mock[NbnRecord]
    when(record.sensitiveOccurrenceRaw).thenReturn(Some("false"))

    val v = new Nbnv67Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv67 should not validate when sensitiveOccurrenceRaw is not null true or false") {
    val record = mock[NbnRecord]
    when(record.sensitiveOccurrenceRaw).thenReturn(Some("fgadg"))

    val v = new Nbnv67Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
