package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.utility.StringParsing._

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 *
 * Check values for vague date type 'D' & 'D>
 */
//todo: write a test for this
class Nbnv73Validator(validator: DateFormatValidator) {

  def validate(record: NbnRecord) = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy")

    validator.validate("NBNV-73", record,true,false,validFormats)
  }
}
