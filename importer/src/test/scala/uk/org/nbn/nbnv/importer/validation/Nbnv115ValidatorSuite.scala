package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv115ValidatorSuite extends BaseFunSuite{
  test("Nbnv115 should not validate a label that is empty") {
    val testAttribute = ("","TestValue")

    val v = new Nbnv115Validator
    val r = v.validate(testAttribute,"RecordKey")

    r.level should be (ResultLevel.ERROR)
  }

  test("Nbnv115 should validate attribute label length up to 50 char") {
    val testAttribute = (("l" * 50 ),"TestValue")

    val v = new Nbnv115Validator
    val r = v.validate(testAttribute,"RecordKey")

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv115 should not validate attribute label length over 50 char") {
    val testAttribute = (("l" * 51 ),"TestValue")

    val v = new Nbnv115Validator
    val r = v.validate(testAttribute,"RecordKey")

    r.level should be (ResultLevel.ERROR)
  }

}
