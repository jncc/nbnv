package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.records.NbnRecord

class Nbnv67Validator {

  def validate(record: NbnRecord) = {
    if (record.sensitiveOccurrenceRaw == null
      || record.sensitiveOccurrenceRaw.toLowerCase == "true"
      || record.sensitiveOccurrenceRaw.toLowerCase == "false") {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: sensitive field is valid"
        def reference = "Record " + record.key
      }
    } else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "The optional sensitive field must be 'true' or 'false' if defined"
        def reference = "Record " + record.key
      }
    }
  }
}
