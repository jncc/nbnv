package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv115Validator {
  val code = "NBNV-115"

  def validate(attribute: (String, String), recordKey: String) = {

    val (label, value) = attribute


    if (label.isEmpty) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: A record attribute name is missing".format(code)
        def reference = recordKey
      }
    }
    else {
      val validator = new LengthValidator
      validator.validate(code, recordKey, "The attribute name '%s'".format(label), label, 50, 1)
    }
  }
}
