package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.BadDataException

class Nbnv66Validator {
  def validate(record: NbnRecord) = {

    if (record.absenceRaw == null
      || record.absenceRaw.toLowerCase == "presence"
      || record.absenceRaw.toLowerCase == "absence") {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Absence is null, presence or absence"
        def reference = record.key
      }
    } else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "The value of the optional absence field must be either 'presence' or 'absence'"
        def reference = record.key
      }
    }
  }
}
