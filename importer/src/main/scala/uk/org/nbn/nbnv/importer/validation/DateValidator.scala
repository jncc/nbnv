package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 */
class DateValidator {

  def validate(record: NbnRecord):ListBuffer[Result] = {
    val resultList = new ListBuffer[Result]

    // Check for a valid DateType
    val v1 = new Nbnv78Validator
    val r1 = v1.validate(record)
    resultList.append(r1)

    // Check if any of the supplied dates are in the future
    val v2 = new Nbnv71Validator
    val r2 = v2.validate(record)
    resultList.append(r2)

    val v3 = new Nbnv72Validator
    val r3 = v3.validate(record)
    resultList.append(r3)

    // We only should every carry on if we have a set of valid dates otherwise these tests make no sense anymore
    val r4 = validateAccordingToType(record)
    resultList.append(r4)

    return resultList
  }

  def validateAccordingToType(record: NbnRecord): Result = record.dateType match {
    case "D"  => val v1 = new Nbnv73Validator; v1.validate(record)
    case "DD" => new Result { def level: ResultLevel.ResultLevel = ResultLevel.DEBUG; def reference: String = record.dateType; def message: String = "Got a valid datetype and date combination" }
    case "O"  => val v1 = new Nbnv74Validator; v1.validate(record)
    case "OO" => val v1 = new Nbnv74Validator; v1.validate(record)
    case "Y"  => val v1 = new Nbnv75Validator; v1.validate(record)
    case "YY" => val v1 = new Nbnv75Validator; v1.validate(record)
    case _    => new Result {def level: ResultLevel.ResultLevel = ResultLevel.ERROR; def reference: String = record.dateType; def message: String = "Got an invalid dateType"}
  }
}
