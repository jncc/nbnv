package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import collection.mutable.ListBuffer

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 *
 * Check values for vague date type 'D'
 */
class Nbnv73Validator extends DateFormatValidator {

  def code = "NBNV-73"

  def validate(record: NbnRecord) = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy","dd-MMM-yyyy","dd/MMM/yyyy")

    val results = new ListBuffer[Result]

    results.appendAll(validateDate(record,true,false,validFormats))

    if (record.startDate.isDefined
      && record.endDate.isDefined
      && record.startDate.get.compareTo(record.endDate.get) != 0) {
      results.append(
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The same date must be used for both the StartDate and EndDate for a date with DateType '%s'".format(code, record.dateType)
        })
    }

    results.toList
  }
}
