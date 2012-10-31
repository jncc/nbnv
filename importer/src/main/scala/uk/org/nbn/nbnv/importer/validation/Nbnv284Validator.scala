package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import java.util.Calendar

//validate "Y" date type
//todo: write some tests for this
class Nbnv284Validator extends DateFormatValidator {
  def validate(record: NbnRecord) = {
    val results = new ListBuffer[Result]

    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy", "MMM yyyy", "yyyy")

    val r2 = validateDate(record,true,false,validFormats)
    results.appendAll(r2)

    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty) {

      val year = Calendar.getInstance()
      year.setTime(record.startDate.get)
      year.set(Calendar.DAY_OF_YEAR, 1)

      if (record.startDate.get.compareTo(year.getTime) != 0) {
        val r3 = new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "The start date is not the start of the year %s".format(year.get(Calendar.YEAR).toString)
        }

        results.append(r3)
      }

      if (record.endDate.isDefined) {
        year.set(Calendar.DAY_OF_YEAR, year.getActualMaximum(Calendar.DAY_OF_YEAR))
        if (record.endDate.get.compareTo(year.getTime) != 0) {
          val r3 = new Result {
            def level: ResultLevel.ResultLevel = ResultLevel.ERROR
            def reference: String = record.key
            def message: String = "The end date is specified and it is not the end of the year %s".format(year.get(Calendar.YEAR).toString)
          }

          results.append(r3)
        }
      }
    }

    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty) {
      val r4 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "Validated: The dates are valid for date type '%s'".format(record.dateType)
      }

      results.append(r4)
    }

    results.toList
  }
}
