package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.utility.StringParsing._


class Nbnv71ValidatorSuite extends BaseFunSuite with BeforeAndAfter {

  var record :NbnRecord = _
  var v : Nbnv71Validator = _

  before {
    record = mock[NbnRecord]
    v = new Nbnv71Validator
  }

  test("should validate start dates in the past") {
    when(record.startDate).thenReturn("30/11/2001".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate start dates in the future") {
    when(record.startDate).thenReturn("30/11/9999".maybeDate("dd/MM/yyyy"))

    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
