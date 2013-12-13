package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import collection.mutable.ListBuffer

class Nbnv77Validator {
  def validate(record: NbnRecord) = {
    val results = new ListBuffer[Result]
    val code = "NBNV-77"

    if (record.eventDateRaw.isDefined) {
      results.append(
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: A date should not be specified for date type '%s'".format(code, record.dateType)
        })
    }
    else if (record.startDate.isDefined) {
      results.append(
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: A start date should not be specified for date type '%s'".format(code, record.dateType)
        })
    }

    if (record.endDate.isDefined) {
      results.append(
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: An end date should not be specified for date type '%s'".format(code, record.dateType)
        })
    }

    if (results.isEmpty) {
      results.append(
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
          def reference: String = record.key
          def message: String = "%s Validated: no date is required for date types '%s'".format(code, record.dateType)
        })
    }

    results.toList
  }
}
