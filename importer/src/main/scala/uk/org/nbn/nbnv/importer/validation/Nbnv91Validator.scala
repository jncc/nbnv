package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv91Validator {
  def validate(record: NbnRecord) = {
    if (record.recorder == null || record.recorder.length <= 140){
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Recorder name is less then or equal to 140 characters"
        def reference = "Record " + record.key
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "Recorder name is greater then 140 characters"
        def reference = "Record " + record.key
      }
    }
  }
}
