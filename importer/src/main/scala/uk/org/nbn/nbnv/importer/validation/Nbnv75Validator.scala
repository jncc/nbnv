package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import collection.mutable.ListBuffer
import java.util.Calendar

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 *
 * Should correct vague date for type 'Y' or 'Y>' or 'Y-'
 */
//todo write a test for this
class Nbnv75Validator extends DateFormatValidator {

  def code = "NBNV-75"

  def validate(record: NbnRecord) = {

    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy", "MMM yyyy", "yyyy")

    val results = new ListBuffer[Result]

    if (record.endDate.isDefined) {
      val r1 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.dateType
        def message: String = "%s: No end date should be specified for date type '%s'".format(code, record.dateType)
      }

      results.append(r1)
    }

    val r2 = validateDate(record,true,false,validFormats)
    results.appendAll(r2)

    //no errors and is not one of the vague date formats like MM yyyy or yyyy
    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty
      && record.startDateRaw.get.matches("""(^\d{2}\s\d{4}$|^\d{4}$)""") == false) {
      val startOfYear = Calendar.getInstance()
      startOfYear.setTime(record.startDate.get)
      startOfYear.set(Calendar.DAY_OF_YEAR, 1)

      if (record.startDate.get.compareTo(startOfYear.getTime) != 0) {
        val r3 = new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = "Record: " + record.key
          def message: String = "%s: The start date is not the start of the year %s".format(code, startOfYear.get(Calendar.YEAR).toString)
        }

        results.append(r3)
      }
    }

    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty) {
      val r3 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = "Record: " + record.key
        def message: String = "Validated: The dates are valid for date type '%s'".format(record.dateType)
      }

      results.append(r3)
    }

    results.toList
  }
}
