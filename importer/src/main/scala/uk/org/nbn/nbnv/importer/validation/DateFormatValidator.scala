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

    if (startDateRequired && record.startDate.isDefined == false) {
      val r1 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: The start date must be specified for date type '%s'".format(code, record.dateType)
      }

      results.append(r1)
    } else if (startDateRequired && record.startDateRaw.get.isValidDate(validDateFormats) == false) {
      val r2 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: The start date is not specific enough for date type '%s'".format(code, record.dateType)
      }

      results.append(r2)
    }

    if (endDateRequired && record.endDate.isDefined == false) {
      val r3 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: The end date must be specified for date type '%s'".format(code, record.dateType)
      }

      results.append(r3)

    } else if (endDateRequired && record.endDateRaw.get.isValidDate(validDateFormats) == false) {
      val r4 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: The end date is not specific enough for date type '%s'".format(code, record.dateType)
      }

      results.append(r4)
    }

    results.toList
  }
}
