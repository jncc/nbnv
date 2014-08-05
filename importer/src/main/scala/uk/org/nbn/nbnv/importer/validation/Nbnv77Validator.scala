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
          def message: String = "%s: The date should not be supplied for a date with DateType '%s'".format(code, record.dateType)
        })
    }
    else if (record.startDate.isDefined) {
      results.append(
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The StartDate should not be supplied for a date with DateType '%s'".format(code, record.dateType)
        })
    }

    if (record.endDate.isDefined) {
      results.append(
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The EndDate should not be supplied for a date with DateType '%s'".format(code, record.dateType)
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
