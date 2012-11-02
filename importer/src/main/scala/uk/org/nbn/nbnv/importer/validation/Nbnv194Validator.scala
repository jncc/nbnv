package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import collection.mutable.ListBuffer

//validate the "<D" date type
class Nbnv194Validator extends DateFormatValidator {
  def code = "NBNV-194"

  def validate(record: NbnRecord) = {

    val results = new ListBuffer[Result]

    if (record.startDate.isDefined) {
      results.append(
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: A start date should not be specified for date type '%s'".format(code, record.dateType)
        })
    }
    else {
      val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy")

      results.appendAll(validateDate(record,false,true,validFormats))
    }

    results.toList
  }
}
