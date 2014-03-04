package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import collection.mutable.ListBuffer

class Nbnv68Validator {

  // Record Date must be of a valid format
  def validate(record: NbnRecord) = {

    val resultList = new ListBuffer[Result]

    if (record.startDateRaw.isDefined)
      resultList.append(validateDate(record.startDateRaw.get, "start", record.key))

    if (record.endDateRaw.isDefined)
      resultList.append(validateDate(record.endDateRaw.get, "end",record.key))

    if (record.eventDateRaw.isDefined)
      resultList.append(validateDate(record.eventDateRaw.get, "event",record.key))

    resultList
  }

  private def validateDate(dateString: String, dateFieldName: String, recordKey: String) = {
    val validFormats = List("dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "dd MMM yyyy","dd-MMM-yyyy","dd/MMM/yyyy", "MMM yyyy", "yyyy")

    var isValid = dateString.isValidDate(validFormats)

    if (isValid) {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = recordKey
        def message: String = "NBNV-68: Validated: '%s' date is a valid date format for the %s date".format(dateString, dateFieldName)
      }
    } else {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = recordKey
        def message: String = "NBNV-68: '%s' is not a valid date format for the %s date".format(dateString, dateFieldName)
      }
    }
  }
}
