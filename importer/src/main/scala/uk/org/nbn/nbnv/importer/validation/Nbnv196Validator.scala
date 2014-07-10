package uk.org.nbn.nbnv.importer.validation

import java.text.SimpleDateFormat

import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import java.util.{Date, Calendar}

//validate OO and MM

//todo write test for this
class Nbnv196Validator extends DateFormatValidator {
  def code = "NBNV-196"

  def validate(record: NbnRecord) = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy","dd-MMM-yyyy","dd/MMM/yyyy", "MMM yyyy")

    val results = new ListBuffer[Result]

    results.appendAll(validateDate(record, true, true, validFormats))

    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty) {
      //start date is start of month
      val startMonth = Calendar.getInstance
      startMonth.setTime(record.startDate.get)
      startMonth.set(Calendar.DAY_OF_MONTH, 1)

      val dateFormat = new SimpleDateFormat("MMMMM yyyy")

      if (record.startDate.get.compareTo(startMonth.getTime) != 0) {
        val r2 = new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The start date is not the start of the month of %s".format(code, dateFormat.format(startMonth.getTime))
        }

        results.append(r2)
      }

      val endMonth = Calendar.getInstance
      endMonth.setTime(record.endDate.get)
      endMonth.set(Calendar.DAY_OF_MONTH, endMonth.getActualMaximum(Calendar.DAY_OF_MONTH))

      if (record.endDate.get.compareTo(endMonth.getTime) != 0 ) {
        val r3 = new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The end date is not the end of the month of %s".format(code, dateFormat.format(endMonth.getTime))
        }

        results.append(r3)
      }

      startMonth.set(Calendar.DAY_OF_MONTH, startMonth.getActualMaximum(Calendar.DAY_OF_MONTH))

      if (record.endDate.get.compareTo(startMonth.getTime) == 0) {
        val r4 = new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The start and end date are from the same month".format(code)
        }
        results.append(r4)
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

    results.toList
  }
}