package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

//validate SiteName field
class Nbnv80Validator {
  def validate(record: NbnRecord) = {
    val validator = new LengthValidator
    validator.validate("NBNV-80", record.key, "SiteName", record.siteName getOrElse "", 100)
    }

}
