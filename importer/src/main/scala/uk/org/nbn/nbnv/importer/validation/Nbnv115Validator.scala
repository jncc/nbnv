package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv115Validator {

  def validate(attribute: (String, String), recordKey: String) = {

    val (label, value) = attribute


    if (label.isEmpty) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-115: An attribute must have a label"
        def reference = recordKey
      }
    }
    else {
      val validator = new LengthValidator
      validator.validate("NBNV-115", label, "Attribute", label, 50, 1)
    }
  }
}
