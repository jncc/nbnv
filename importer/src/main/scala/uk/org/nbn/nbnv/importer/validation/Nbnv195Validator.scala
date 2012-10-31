package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord

//todo: write a test for this
class Nbnv195Validator extends DateFormatValidator {
  def code = "NBNV-195"

  def validate(record:NbnRecord) = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy")

    validateDate(record, true, true, validFormats)
  }
}
