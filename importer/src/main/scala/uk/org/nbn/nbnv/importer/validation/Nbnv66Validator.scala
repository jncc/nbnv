package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.ImportFailedException

class Nbnv66Validator {
  def validate(record: NbnRecord) = {

    if (record.absenceText == null
      || record.absenceText.toLowerCase == "presence"
      || record.absenceText.toLowerCase == "absence") {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Absence is null, presence or absence"
        def reference = "Record " + record.key
      }
    } else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "The value of the optional absence field must be either 'presence' or 'absence'"
        def reference = "Record " + record.key
      }
    }
  }
}
