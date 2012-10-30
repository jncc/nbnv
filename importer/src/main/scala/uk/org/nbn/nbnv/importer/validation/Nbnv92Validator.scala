package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv92Validator {
  def validate(record: NbnRecord) = {
    val validator = new LengthValidator
    validator.validate(record.key, "Determiner", record.determiner getOrElse "", 140)
  }
}
