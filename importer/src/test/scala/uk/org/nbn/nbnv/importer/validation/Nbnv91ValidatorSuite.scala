package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv91ValidatorSuite extends BaseFunSuite{
  test("Nbnv91 should validate null recorder names") {
    val record = mock[NbnRecord]
    when(record.recorder).thenReturn(None)

    val v = new Nbnv91Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv91 should validate recorder names of 140 characters of less") {
    val record = mock[NbnRecord]
    when(record.recorder).thenReturn(Some("a" * 140))

    val v = new Nbnv91Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv91 should mot validate recorder names greater then 140 characters") {
    val record = mock[NbnRecord]
    when(record.recorder).thenReturn(Some("a" * 141))

    val v = new Nbnv91Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
