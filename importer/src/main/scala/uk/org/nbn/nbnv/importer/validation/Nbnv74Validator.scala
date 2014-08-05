package uk.org.nbn.nbnv.importer.validation

import java.text.SimpleDateFormat

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import collection.mutable.ListBuffer
import java.util.{Date, Calendar}

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 *
 * Check for a valid O / M vague date type
 */
class Nbnv74Validator extends DateFormatValidator {

  def code = "NBNV-74"

  def validate(record: NbnRecord) = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy","dd-MMM-yyyy","dd/MMM/yyyy", "MMM yyyy")

    val results = new ListBuffer[Result]

    val r1 =  validateDate(record, true, false, validFormats)
    results.appendAll(r1)

    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty) {
      //start date is start of month
      val month = Calendar.getInstance
      month.setTime(record.startDate.get)
      month.set(Calendar.DAY_OF_MONTH, 1)

      val dateFormat = new SimpleDateFormat("MMMMM yyyy")

      if (record.startDate.get.compareTo(month.getTime) != 0) {
        results.append(new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The StartDate must be the first day of the specified month for a date with DateType '%s'".format(code, record.dateType)
        })
      }

      //if specified end date is end of month
      if (record.endDate.isDefined) {
        month.set(Calendar.DAY_OF_MONTH, month.getActualMaximum(Calendar.DAY_OF_MONTH))

        if (record.endDate.get.compareTo(month.getTime) != 0 ) {
          results.append(new Result {
            def level: ResultLevel.ResultLevel = ResultLevel.ERROR
            def reference: String = record.key
            def message: String = "%s: The EndDate must be the last day of the specified month for a date with DateType '%s''".format(code, record.dateType)
          })
        }

        val currentCal = Calendar.getInstance()
        currentCal.setTime(new Date())
        currentCal.set(Calendar.DAY_OF_YEAR, currentCal.getActualMaximum(Calendar.DAY_OF_YEAR))

        if (record.endDate.get.after(currentCal.getTime)) {
          results.append(new Result {
            def level: ResultLevel.ResultLevel = ResultLevel.ERROR
            def reference: String = record.key
            def message: String = "%s: The EndDate must not be in the future".format(code)
          })
        }
      }
    }

    results.toList
  }
}
