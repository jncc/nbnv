package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import collection.mutable.ListBuffer

//todo:needs some tests
class Nbnv68Validator {

  // Record Date must be of a valid format
  def validate(record: NbnRecord) = {

    val resultList = new ListBuffer[Result]

    if (record.startDateRaw.isDefined)
      resultList.append(validateDate(record.startDateRaw.get, "Start", record.key))

    if (record.endDateRaw.isDefined)
      resultList.append(validateDate(record.endDateRaw.get, "End",record.key))

    resultList
  }

  private def validateDate(dateString: String, resultPrefix: String, recordKey: String) = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy", "MMM yyyy", "yyyy")

    var isValid = false

    validFormats.toStream
      .takeWhile(_ => isValid == false)
      .foreach(df => if (dateString.maybeDate(df) != None) isValid = true)

    if (isValid) {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = recordKey
        def message: String = "Validated: %s date is a valid date format".format(resultPrefix)
      }
    } else {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = recordKey
        def message: String = "%s is not a valid date format".format(resultPrefix)
      }
    }
  }
}
