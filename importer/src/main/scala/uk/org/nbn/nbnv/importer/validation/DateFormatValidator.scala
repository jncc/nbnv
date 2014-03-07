package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import collection.mutable.ListBuffer

//todo: write a test for this
abstract class DateFormatValidator {

  def validate(record: NbnRecord): List[Result]

  protected def code : String

  protected def validateDate(record: NbnRecord
               , startDateRequired: Boolean
               , endDateRequired: Boolean
               , validDateFormats: List[String]) = {

    val results = new ListBuffer[Result]
    val dateType = record.dateType.getOrElse("n/a")

    if (startDateRequired && record.startDate.isDefined == false) {
      val r1 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: The start date must be specified for date type '%s'".format(code, dateType)
      }

      results.append(r1)
    } else if (startDateRequired &&
      ((record.startDateRaw.isDefined && record.startDateRaw.get.isValidDate(validDateFormats) == false)
        || (record.eventDateRaw.isDefined && record.eventDateRaw.get.isValidDate(validDateFormats) == false))) {
      val r2 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: The start date format is not valid for date type '%s'".format(code, dateType)
      }

      results.append(r2)
    }

    if (endDateRequired && record.endDate.isDefined == false) {
      val r3 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: The end date must be specified for date type '%s'".format(code, dateType)
      }

      results.append(r3)

    } else if (endDateRequired && record.endDateRaw.get.isValidDate(validDateFormats) == false) {
      val r4 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: The end date format is not valid for date type '%s'".format(code, dateType)
      }

      results.append(r4)
    }

    results.toList
  }
}
