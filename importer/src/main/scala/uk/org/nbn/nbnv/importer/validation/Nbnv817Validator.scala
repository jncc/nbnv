package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv817Validator {
  def validate(record: NbnRecord) = {
    if ((record.startDateRaw.isDefined || record.endDateRaw.isDefined) & record.eventDateRaw.isDefined) {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "NBNV-817: Invalid date field combination. Either the start and end date fields should be used or the date field should be used."
      }
    } else {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "NBNV-817: Validated: The combination of date fields is valid"
      }
    }

  }

}
