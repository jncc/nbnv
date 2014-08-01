package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import java.util.Date
import collection.mutable.ListBuffer

//Correct vague date for datetype "DD"
//todo: write a test for this
class Nbnv195Validator extends DateFormatValidator {
  def code = "NBNV-195"

  def validate(record:NbnRecord) = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy","dd-MMM-yyyy","dd/MMM/yyyy")

    val results = new ListBuffer[Result]
    results.appendAll(validateDate(record, true, true, validFormats))

    val current = new Date()
    if (record.endDate.isDefined && record.endDate.get.after(current)) {
      results.append(new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: The EndDate must not be in the future".format(code)
      })
    }

    results.toList
  }
}
