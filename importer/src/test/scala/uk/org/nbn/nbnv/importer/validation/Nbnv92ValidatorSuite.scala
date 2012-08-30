package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv92ValidatorSuite extends BaseFunSuite{
  test("Nbnv92 should validate null determiner names") {
    val record = mock[NbnRecord]
    when(record.determiner).thenReturn(null)

    val v = new Nbnv92Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv92 should validate determiner names of 140 characters of less") {
    val record = mock[NbnRecord]
    when(record.determiner).thenReturn("a" * 140)

    val v = new Nbnv92Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv92 should mot determiner names greater then 140 characters") {
    val record = mock[NbnRecord]
    when(record.determiner).thenReturn("a" * 141)

    val v = new Nbnv92Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
