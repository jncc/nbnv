package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class NullFieldValidator {
  def validate(code: String, value: String, term: String) = {
    if (value == null) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: The '%s' field is mandatory".format(code, term)
        def reference = ""
      }
    }
    else {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: the '%s' field is present".format(code, term)
        def reference = ""
      }
    }
  }
}
