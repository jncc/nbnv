package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

//validate Recorder field length
class Nbnv91Validator {
  def validate(record: NbnRecord) = {
    val validator = new LengthValidator
    validator.validate("NBNV-91", record.key, "This Recorder", record.recorder getOrElse "", 140)
  }
}
