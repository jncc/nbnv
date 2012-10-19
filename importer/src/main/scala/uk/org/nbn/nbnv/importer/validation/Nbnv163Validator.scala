package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}
import uk.org.nbn.nbnv.importer.records.NbnRecord

class Nbnv163Validator {
  // Validate a record based on the presence of its record key, if its there then we are good to go, if the key is
  // an empty string or whitespace then fail the validation of this record
  def validate(record:NbnRecord) = {

    def success = {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "RecordKey was present"
        def reference = "Record " + record.key
      }
    }

    def fail = {
      new Result {
        def level = ResultLevel.ERROR
        def message = "RecordKey was not present"
        def reference = ""
      }
    }

    if (record.key.trim != "")
      success else fail
  }
}