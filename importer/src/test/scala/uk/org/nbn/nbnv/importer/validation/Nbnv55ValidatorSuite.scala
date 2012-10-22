package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv55ValidatorSuite extends BaseFunSuite {
  test("Nbnv55 should not validate if recordkey is null") {
    val record = mock[NbnRecord]
    when(record.key).thenReturn(null)

    val v = new Nbnv55Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("Nbnv55 should validate if recordkey is not null") {
    val record = mock[NbnRecord]
    when(record.key).thenReturn("testKey")

    val v = new Nbnv55Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }
}
