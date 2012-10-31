package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import java.text.SimpleDateFormat
import java.awt.CardLayout
import java.util.Calendar
//todo: write a test for this
//validate the <Y ad -Y date types
class Nbnv76Validator(validator: DateFormatValidator) {

  def validate(record: NbnRecord): Result = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy", "MMM yyyy", "yyyy")

    validator.validate("NBNV-76", record,false,true,validFormats)
  }
}
