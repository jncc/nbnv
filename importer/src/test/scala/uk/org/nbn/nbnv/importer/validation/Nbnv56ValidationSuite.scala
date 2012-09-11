package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite

class Nbnv56ValidationSuite extends BaseFunSuite{
  test("Nbnv56 should not validate if taxonVersionKey is null") {
    val record = mock[NbnRecord]
    when(record.taxonVersionKey).thenReturn(null)

    val v = new Nbnv56Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("Nbnv56 should validate if taxonVersionKey is not null") {
    val record = mock[NbnRecord]
    when(record.taxonVersionKey).thenReturn("testTaxonVersionKey")

    val v = new Nbnv56Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }
}
