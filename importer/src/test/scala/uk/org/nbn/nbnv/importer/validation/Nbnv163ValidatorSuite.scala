package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv163ValidatorSuite extends BaseFunSuite {
  test("should validate a key less then 100 char") {
    val record = mock[NbnRecord]
    when(record.key).thenReturn("GA001268")

    val v = new Nbnv163Validator
    val r = v.validate(record, 1)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should validate a key of 100 char") {
    val record = mock[NbnRecord]
    when(record.key).thenReturn("a" * 100)

    val v = new Nbnv163Validator
    val r = v.validate(record, 1)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate a blank key") {
    val record = mock[NbnRecord]
    when(record.key).thenReturn("")

    val v = new Nbnv163Validator
    val r = v.validate(record, 1)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate a key over 100 char in length") {
    val record = mock[NbnRecord]
    when(record.key).thenReturn("a" * 101)

    val v = new Nbnv163Validator
    val r = v.validate(record, 1)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }
}
