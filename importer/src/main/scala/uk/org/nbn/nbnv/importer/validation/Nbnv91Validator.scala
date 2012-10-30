package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv91Validator {
  def validate(record: NbnRecord) = {
    val validator = new LengthValidator
    validator.validate(record.key, "Recorder", record.recorder getOrElse "", 140)
  }
}
