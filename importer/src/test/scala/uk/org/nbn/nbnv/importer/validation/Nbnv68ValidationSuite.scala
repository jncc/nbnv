package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import java.text.SimpleDateFormat
import uk.org.nbn.nbnv.importer.ImportFailedException

class Nbnv68ValidationSuite extends BaseFunSuite {
  val validator = new Nbnv68Validator()

  test("Should validate a correct date string") {
    val compDate = new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012")


    validator.validate("16/10/2012") should be (compDate)
    validator.validate("16-10-2012") should be (compDate)
    validator.validate("2012/10/16") should be (compDate)
    validator.validate("2012-10-16") should be (compDate)
    validator.validate("16 OCT 2012") should be (compDate)


    validator.validate("OCT 2012") should be (new SimpleDateFormat("MMM yyyy").parse("OCT 2012"))
    validator.validate("2012") should be (new SimpleDateFormat("yyyy").parse("2012"))
  }

  test ("Should return a null value as appropriate") {
    validator.validate(null) should be (null)
    validator.validate("") should be (null)
  }

  test("Should error on an incorrect date string") {
    intercept[ImportFailedException] {
      validator.validate("Utter Rubbish")
    }
  }
}
