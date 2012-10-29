package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.utility.StringParsing._

//todo: write a test for this
class DateFormatValidator {
  def validate(record: NbnRecord
               , startDateRequired: Boolean
               , endDateRequired: Boolean
               , validDateFormats: List[String]) = {
    if (startDateRequired && record.startDate.isDefined == false) {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.dateType
        def message: String = "The start date must be specified for date type '%s'".format(record.dateType)
      }
    } else  if (endDateRequired && record.endDate.isDefined == false) {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.dateType
        def message: String = "The end date must be specified for date type '%s'".format(record.dateType)
      }
    } else {
      if (startDateRequired && record.startDateRaw.get.isValidDate(validDateFormats) == false) {
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.dateType
          def message: String = "The start date is not specific enough for date type '%s'".format(record.dateType)
        }
      } else if (endDateRequired && record.endDateRaw.get.isValidDate(validDateFormats) == false) {
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.dateType
          def message: String = "The end date is not specific enough for date type '%s'".format(record.dateType)
        }
      } else {
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
          def reference: String = record.dateType
          def message: String = "Validated: The start and end date is valid for date type '%s'".format(record.dateType)
        }
      }
    }
  }
}
