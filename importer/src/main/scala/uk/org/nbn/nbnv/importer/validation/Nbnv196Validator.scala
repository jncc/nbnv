package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.Result

//validate OO and MM

//todo write test for this
class Nbnv196Validator extends DateFormatValidator {
  def code = "NBNV-196"

  def validate(record: NbnRecord) = {
    val validFormats = List("MMM yyyy")

    val results = new ListBuffer[Result]

    results.appendAll(validateDate(record, true, true, validFormats))

    results.toList
  }

}
