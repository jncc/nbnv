package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}
import java.util.{Date, Calendar}
import collection.mutable.ListBuffer


class Nbnv197Validator extends DateFormatValidator {
  def code = "NBNV-197"

  def validate(record: NbnRecord) = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd","dd-MMM-yyyy","dd/MMM/yyyy","dd MMM yyyy", "MMM yyyy", "yyyy")

    val results = new ListBuffer[Result]
    results.appendAll(validateDate(record, true, true, validFormats))

    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty) {
      //Start date must be start of a year
      val startCal = Calendar.getInstance
      startCal.setTime(record.startDate.get)
      startCal.set(Calendar.DAY_OF_YEAR,1)

      val startOfyear = startCal.getTime

      if (startOfyear.compareTo(record.startDate.get) == 0) {
        results.append(new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
          def reference: String = record.key
          def message: String = "%s: The start date is the start of the year as required for date type code '%s'".format(code, record.dateType)
        })
      } else {
        results.append(new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The StartDate must be the first day of the year for a date with DateType '%s'".format(code, record.dateType)
        })
      }

      val endCal = Calendar.getInstance
      endCal.setTime(record.endDate.get)
      endCal.add(Calendar.YEAR,1);
      endCal.set(Calendar.DAY_OF_YEAR,1);
      endCal.add(Calendar.DAY_OF_WEEK,-1);   //last day of the year.

      val endOfYear = endCal.getTime

      if (endOfYear.compareTo(record.endDate.get) == 0) {
        results.append( new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
          def reference: String = record.key
          def message: String = "%s: The end date is the end of the year as required for date type code '%s'".format(code, record.dateType)
        })
      } else {
        results.append(new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The EndDate must be the last day of the year for a date with DateType '%s'".format(code, record.dateType)
        })
      }

      startCal.set(Calendar.DAY_OF_YEAR,startCal.getActualMaximum(Calendar.DAY_OF_YEAR))
      if (endOfYear.compareTo(startCal.getTime) == 0) {
        results.append(new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The StartDate and EndDate must occur in different years for a date with DateType '%s'".format(code, record.dateType)
        })
      }

      val currentCal = Calendar.getInstance()
      currentCal.setTime(new Date())
      currentCal.set(Calendar.DAY_OF_YEAR, currentCal.getActualMaximum(Calendar.DAY_OF_YEAR))

      if (record.endDate.get.after(currentCal.getTime)) {
        results.append(new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The EndDate must not be in a future year".format(code)
        })
      }
    }

    results.toList
  }
}
