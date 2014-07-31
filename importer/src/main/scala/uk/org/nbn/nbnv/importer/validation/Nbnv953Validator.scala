package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.metadata.Metadata

class Nbnv953Validator {
  val code = "NBNV-953"

  def validate(metadata: Metadata) = {
    val validator = new LengthValidator()
    validator.validate(code, metadata.administratorEmail, "The administrator email address", metadata.administratorEmail, 70)
  }
}
