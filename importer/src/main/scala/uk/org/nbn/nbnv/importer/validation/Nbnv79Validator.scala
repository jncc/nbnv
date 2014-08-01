package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}

//validate SiteKey length
class Nbnv79Validator {

  def validate(record: NbnRecord) = {
    val validator = new LengthValidator
    validator.validate("NBNV-79", record.key, "This SiteKey", record.siteKey getOrElse "", 30)
  }
}
