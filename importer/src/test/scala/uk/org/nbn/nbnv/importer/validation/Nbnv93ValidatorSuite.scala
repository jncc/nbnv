package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv93ValidatorSuite extends BaseFunSuite{
   test("Nbnv93 should validate attribute value length up to 255 char") {
     val testAttribute = ("TestAttribute",("t" * 255))

     val v = new Nbnv93Validator
     val r = v.validate(testAttribute,"RecordKey")

     r.level should be (ResultLevel.DEBUG)
   }

   test("Nbnv93 should not validate attribute value length over 255 char") {
     val testAttribute = ("TestAttribute",("t" * 256))

     val v = new Nbnv93Validator
     val r = v.validate(testAttribute,"RecordKey")

     r.level should be (ResultLevel.ERROR)
   }
}
