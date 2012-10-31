package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.utility.StringParsing._

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 *
 * Should correct vague date for type 'Y' or 'Y>' or 'Y-'
 */
//todo write a test for this
class Nbnv75Validator(validator: DateFormatValidator) {

  def validate(record: NbnRecord) = {

    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy", "MMM yyyy", "yyyy")

    validator.validate("NBNV-75", record,true, false, validFormats)
  }
}
