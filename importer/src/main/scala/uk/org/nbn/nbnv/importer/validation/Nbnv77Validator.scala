package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import collection.mutable.ListBuffer

class Nbnv77Validator {
  def validate(record: NbnRecord) = {
    val results = new ListBuffer[Result]

    if (record.startDate.isDefined) {
      results.append(
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "NBNV-77: A start date should not be specified for date type '%s'".format(record.dateType)
        })
    }

    if (results.isEmpty) {
      results.append(
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
          def reference: String = record.key
          def message: String = "NBNV-77 Validated: no date is required for date types '%s'".format(record.dateType)
        })
    }

    results.toList
  }
}
