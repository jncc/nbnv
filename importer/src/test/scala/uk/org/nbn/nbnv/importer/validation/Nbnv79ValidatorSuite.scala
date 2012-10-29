package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv79ValidatorSuite extends BaseFunSuite {

  test("Nbnv79 should validate null site key")
  {
    val record = mock[NbnRecord]
    when(record.siteKey).thenReturn(None)

    val v = new Nbnv79Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv79 should validate site key of 30 chars or less")
  {
    val record = mock[NbnRecord]
    when(record.siteKey).thenReturn(Option("z" * 30))

    val v = new Nbnv79Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv79 should not validate site key over 30 chars")
  {
    val record = mock[NbnRecord]
    when(record.siteKey).thenReturn(Option("z" * 40))

    val v = new Nbnv79Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
