package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord

class Nbnv197Validator extends DateFormatValidator {
  def validate(record: NbnRecord) = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy", "MMM yyyy", "yyyy")

    validateDate(record, true, true, validFormats)
  }
}
