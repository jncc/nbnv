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

  def validate(record: NbnRecord) = {

    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy", "MMM yyyy", "yyyy")

    val results = new ListBuffer[Result]

    if (record.endDate.isDefined) {
      val r1 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.dateType
        def message: String = "No end date should be specified for date type '%s'".format(record.dateType)
      }

      results.append(r1)
    }

    val r2 = validateDate(record,true,false,validFormats)
    results.appendAll(r2)

    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty) {
      val startOfYear = Calendar.getInstance()
      startOfYear.setTime(record.startDate.get)
      startOfYear.set(Calendar.DAY_OF_YEAR, 1)

      if (record.startDate.get.compareTo(startOfYear.getTime) != 0) {
        val r3 = new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.dateType
          def message: String = "The start date is not the start of the year %s".format(startOfYear.get(Calendar.YEAR).toString)
        }

        results.append(r3)
      }
    }

    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty) {
      val r3 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.dateType
        def message: String = "Validated: The dates are valid for date type '%s'".format(record.dateType)
      }

      results.append(r3)
    }

    results.toList
  }
}
