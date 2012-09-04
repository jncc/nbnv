package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}

class LengthValidator {
  def validate(reference: String, term: String, value: String, maxLength: Int, minLength:Int = 0) = {
    if (value == null || value.length <= maxLength) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: '%s' has a valid length.".format(term)
        def reference = reference
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "'%s' is greater then the maximum length of %s".format(term, maxLength)
        def reference = reference
      }
    }
  }



}
