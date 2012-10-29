package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv63ValidatorSuite extends BaseFunSuite{
  test("Nvnv63 should validate null sample keys") {
    val record = mock[NbnRecord]
    when(record.sampleKey).thenReturn(None)

    val v = new Nbnv63Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv63 should validate samples keys of 30 chars or less") {
    val record = mock[NbnRecord]
    when(record.sampleKey).thenReturn(Some("a" * 30))

    val v = new Nbnv63Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv63 should not validate sample keys greater then 30 chars") {
    val record = mock[NbnRecord]
    when(record.sampleKey).thenReturn(Some("a" * 31))

    val v = new Nbnv63Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
