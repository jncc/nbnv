package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv57Validator {
  def validate(record: NbnRecord) = {
    if ((record.startDateRaw != null && record.dateType != null)
      || (record.endDateRaw != null && record.dateType != null)) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: A record date field has been defined."
        def reference = ""
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "A record date field must be defined. Either a darwin core eventDate field or an NBN eventDate field must be specified."
        def reference = ""
      }
    }
  }
}

