package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}

class Nbnv93Validator {
  def validate(attribute:(String, String), recordKey: String) = {
    val (label, value) = attribute

    if (value.isEmpty || value.length <= 255) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Attribute '%s' value is less then or equal to 255 characters".format(label)
        def reference = "Record " + recordKey
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "Attribute '%s' value is greater then 255 characters".format(label)
        def reference = "Record " + recordKey
      }
    }
  }

}
