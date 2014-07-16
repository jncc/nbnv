package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv817Validator {
  def validate(record: NbnRecord) = {
    val code = "NBNV-817"

    if ((record.startDateRaw.isDefined || record.endDateRaw.isDefined) & record.eventDateRaw.isDefined) {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: Either the Date or Vague Date fields must be supplied for this record".format(code)
      }
    } else {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "%s: Validated: The combination of date fields is valid".format(code)
      }
    }

  }

}
