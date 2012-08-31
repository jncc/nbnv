package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv115Validator {
  def validate(attribute:(String, String), recordKey: String) = {
    val (label, value) = attribute

    if (label.isEmpty) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "An attribute must have a label"
        def reference = "Record " + recordKey
      }
    }
    else if (label.length <= 50) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Attribute '%s' label is less then or equal to 50 characters".format(label)
        def reference = "Record " + recordKey
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "Attribute '%s' label is greater then 50 characters".format(label)
        def reference = "Record " + recordKey
      }
    }
  }
}
