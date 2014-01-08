package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.records.NbnRecord

class Nbnv878ValidatorSuite extends BaseFunSuite with BeforeAndAfter {

  var record : NbnRecord = _
  var v : Nbnv878Validator = _

  before {
    record = mock[NbnRecord]
    v = new Nbnv878Validator
  }

  test("should validate valid attribute json") {
    when(record.attributesRaw)thenReturn(Some("{\"Source\":\"Badger Sett Records\"}"))

    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate invalid attribute json") {
    when(record.attributesRaw)thenReturn(Some("{\"Source:\"Badger Sett Records\"}"))

    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("should validate a record with no attributes") {
    when(record.attributesRaw)thenReturn(None)

    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

}
