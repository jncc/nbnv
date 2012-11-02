package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import java.text.SimpleDateFormat
import java.awt.CardLayout
import java.util.Calendar
import collection.mutable.ListBuffer

//validate the <Y ad -Y date types
class Nbnv76Validator extends DateFormatValidator {

  def code = "NBNV-76"

  def validate(record: NbnRecord) = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy", "MMM yyyy", "yyyy")
    val results = new ListBuffer[Result]

    if (record.startDate.isDefined) {
      val r1 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: No start date should be specified for date type '%s'".format(code, record.dateType)
      }

      results.append(r1)
    }

    val r2 = validateDate(record,false,true,validFormats)
    results.appendAll(r2)

    //no errors and is not one of the vague date formats like MM yyyy or yyyy
    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty
      && record.endDateRaw.get.matches("""(^\d{2}\s\d{4}$|^\d{4}$)""") == false) {
      val endOfYear = Calendar.getInstance()
      endOfYear.setTime(record.endDate.get)
      endOfYear.set(Calendar.DAY_OF_YEAR, endOfYear.getActualMaximum(Calendar.DAY_OF_YEAR))

      if (record.endDate.get.compareTo(endOfYear.getTime) != 0) {
        val r3 = new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The end date is not the end of the year %s".format(code, endOfYear.get(Calendar.YEAR).toString)
        }

        results.append(r3)
      }
    }

    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty) {
      val r3 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "Validated %s: The dates are valid for date type '%s'".format(code, record.dateType)
      }

      results.append(r3)
    }

    results.toList

  }
}
