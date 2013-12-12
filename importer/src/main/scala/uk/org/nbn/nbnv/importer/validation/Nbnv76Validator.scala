package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import java.text.SimpleDateFormat
import java.awt.CardLayout
import java.util.Calendar
import collection.mutable.ListBuffer
import java.util.Date

//validate the <Y ad -Y date types
class Nbnv76Validator extends DateFormatValidator {

  def code = "NBNV-76"

  def validate(record: NbnRecord) = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy","dd-MMM-yyyy","dd/MMM/yyyy", "MMM yyyy", "yyyy")
    val results = new ListBuffer[Result]

    if (record.startDate.isDefined) {
      val r1 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: A start date must not be specified for date type '%s'".format(code, record.dateType)
      }

      results.append(r1)
    }

    val r2 = validateDate(record,false,true,validFormats)
    results.appendAll(r2)

    if (record.endDate.isDefined) {
      val endOfYear = Calendar.getInstance()
      endOfYear.setTime(record.endDate.get)
      endOfYear.set(Calendar.DAY_OF_YEAR, endOfYear.getActualMaximum(Calendar.DAY_OF_YEAR))

      if (record.endDate.get.compareTo(endOfYear.getTime) != 0) {
        results.append(new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The end date is not the end of the year %s".format(code, endOfYear.get(Calendar.YEAR).toString)
        })
      }

      val currentCal = Calendar.getInstance()
      currentCal.setTime(new Date())
      currentCal.set(Calendar.DAY_OF_YEAR, currentCal.getActualMaximum(Calendar.DAY_OF_YEAR))

      if (record.endDate.get.after(currentCal.getTime)) {
        results.append(new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The end date cannot be after the end of the current year".format(code)
        })
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
