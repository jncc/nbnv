package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord

class Nbnv56Validator {
  def validate(record: NbnRecord) = {
    val validator = new NullFieldValidator
    validator.validate("NBNV-56", record.taxonVersionKey, "TaxonVersionKey")
  }
}
