package uk.org.nbn.nbnv.importer.validation

import java.text.SimpleDateFormat

import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import java.util.{Date, Calendar}

//validate "Y" date type
//todo: write some tests for this
class Nbnv284Validator extends DateFormatValidator {
  def code = "NBNV-284"

  def validate(record: NbnRecord) = {
    val results = new ListBuffer[Result]

    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy","dd-MMM-yyyy","dd/MMM/yyyy", "MMM yyyy", "yyyy")

    val r2 = validateDate(record,true,false,validFormats)
    results.appendAll(r2)

    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty) {

      val year = Calendar.getInstance()
      year.setTime(record.startDate.get)
      year.set(Calendar.DAY_OF_YEAR, 1)

      val dateFormat = new SimpleDateFormat("yyyy")

      if (record.startDate.get.compareTo(year.getTime) != 0) {
        results.append(new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The StartDate must be the first day of the specified year for a date with DateType '%s'".format(code, record.dateType)
        })
      }

      if (record.endDate.isDefined) {
        year.set(Calendar.DAY_OF_YEAR, year.getActualMaximum(Calendar.DAY_OF_YEAR))
        if (record.endDate.get.compareTo(year.getTime) != 0) {
          results.append(new Result {
            def level: ResultLevel.ResultLevel = ResultLevel.ERROR
            def reference: String = record.key
            def message: String = "%s: The EndDate must be the last day of the specified year for a date with DateType '%s'".format(code, record.dateType)
          })
        }

        val currentCal = Calendar.getInstance()
        currentCal.setTime(new Date())
        currentCal.set(Calendar.DAY_OF_YEAR, currentCal.getActualMaximum(Calendar.DAY_OF_YEAR))

        if (record.endDate.get.after(currentCal.getTime)) {
          results.append(new Result {
            def level: ResultLevel.ResultLevel = ResultLevel.ERROR
            def reference: String = record.key
            def message: String = "%s: The EndDate must not be in a future year".format(code, record.dateType)
          })
        }
      }
    }

    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty) {
      results.append(new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "%s: Validated: The dates are valid for date type '%s'".format(code, record.dateType)
      })
    }

    results.toList
  }
}
