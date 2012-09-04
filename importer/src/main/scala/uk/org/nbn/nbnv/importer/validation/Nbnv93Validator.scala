package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}

class Nbnv93Validator {
  def validate(attribute:(String, String), recordKey: String) = {
    val (label, value) = attribute

    val validator = new LengthValidator
    validator.validate(recordKey, label, value, 255)

  }

}
