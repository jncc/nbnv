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
 * Check for a valid O / M vague date type
 */
//todo: write a test for this
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

      if (record.startDate.get.compareTo(month.getTime) != 0) {
        val r2 = new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The start date is not the start of the month of %s".format(code, month.get(Calendar.MONTH).toString)
        }

        results.append(r2)
      }

      //if specified end date is end of month
      if (record.endDate.isDefined) {
        month.set(Calendar.DAY_OF_MONTH, month.getActualMaximum(Calendar.DAY_OF_MONTH))

        if (record.endDate.get.compareTo(month.getTime) != 0 ) {
          val r3 = new Result {
            def level: ResultLevel.ResultLevel = ResultLevel.ERROR
            def reference: String = record.key
            def message: String = "%s: The end date is specified but it is not the end of the month of %s".format(code, month.get(Calendar.MONTH).toString)
          }

          results.append(r3)
        }
      }
    }

    results.toList
  }
}
