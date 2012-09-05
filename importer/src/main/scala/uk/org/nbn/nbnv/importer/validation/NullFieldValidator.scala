package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class NullFieldValidator {
  def validate(value: String, term: String) = {
    if (value == null) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "The '%s' field is mandatory".format(term)
        def reference = ""
      }
    }
    else {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: the '%s' field is present".format(term)
        def reference = ""
      }
    }
  }
}
