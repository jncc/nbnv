package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.utility.StringParsing._

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 *
 * Check for a valid O / M vague date type
 */
//todo: write a test for this
class Nbnv74Validator(validator: DateFormatValidator) {

  def validate(record: NbnRecord) = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy", "MMM yyyy")

    validator.validate(record, true, false, validFormats)
  }
}
