package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

//todo: test this
class Nbnv217Validator extends DateFormatValidator {
  def code = "NBNV-217"

  def validate(record: NbnRecord) = {


    val results = new ListBuffer[Result]
    if (record.endDate.isDefined) {
      results.append(
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: An end date should not be specified for date type '%s'".format(code, record.dateType)
        })
    }
    else {

      val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy")

      results.appendAll(validateDate(record,true,false,validFormats))
    }

    results.toList
  }

}
