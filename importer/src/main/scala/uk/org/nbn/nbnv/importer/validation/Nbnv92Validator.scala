package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

//validate Determiner field length
class Nbnv92Validator {
  def validate(record: NbnRecord) = {
    val validator = new LengthValidator
    validator.validate("NBNV-92", record.key, "This Determiner", record.determiner getOrElse "", 140)
  }
}
