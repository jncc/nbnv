package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv80ValidatorSuite extends BaseFunSuite{
  test("Nbnv80 should validate null site names") {
    val record = mock[NbnRecord]
    when(record.siteName).thenReturn(null)

    val v = new Nbnv80Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv80 should validate site names of 100 characters of less") {
    val record = mock[NbnRecord]
    when(record.siteName).thenReturn("a" * 100)

    val v = new Nbnv80Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv80 should mot validate site names greater then 100 characters") {
    val record = mock[NbnRecord]
    when(record.siteName).thenReturn("a" * 101)

    val v = new Nbnv80Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
