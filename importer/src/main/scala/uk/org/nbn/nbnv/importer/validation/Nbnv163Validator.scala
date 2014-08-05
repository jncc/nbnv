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

    val code = "NBNV-163"
    val results = new ListBuffer[Result]

    if (record.key.trim != "") {
      results.append(new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: RecordKey was present".format(code)
        def reference = "record index: %d".format(index)
      })
    } else {
      results.append(new Result {
        def level = ResultLevel.ERROR
        def message = "%s: The RecordKey field is required".format(code)
        def reference = "record index: %d".format(index)
      })
    }

    if (results.find(r => r.level == ResultLevel.ERROR).isEmpty) {
      val validator = new LengthValidator()
      results.append(validator.validate(code, record.key, "This RecordKey", record.key, 100))
    }

    results.toList
  }
}
