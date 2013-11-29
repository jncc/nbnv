package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}
import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer

//validate RecordKey is provided
class Nbnv163Validator {
  // Validate a record based on the presence of its record key, if its there then we are good to go, if the key is
  // an empty string or whitespace then fail the validation of this record.
  // Check the record key is not over 100 char in length
  def validate(record:NbnRecord, index:Integer) = {

    val results = new ListBuffer[Result]

    if (record.key.trim != "") {
      results.append(new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-163: RecordKey was present"
        def reference = "record index: %d".format(index)
      })
    } else {
      results.append(new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-163: RecordKey was not present"
        def reference = "record index: %d".format(index)
      })
    }

    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty) {
      if (record.key.length > 100) {
        results.append(new Result {
          def level = ResultLevel.ERROR
          def message = "NBNV-163: The maximum length of the record key is 100 characters. RecordKey is %d characters in lengh.".format(record.key.length)
          def reference = record.key
        })
      } else {
        results.append(new Result {
          def level = ResultLevel.DEBUG
          def message = "NBNV-163: The record key lenght is ok"
          def reference = record.key
        })
      }
    }

    results.toList
  }
}
