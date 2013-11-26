package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 */
class DateValidator {

  def validate(record: NbnRecord) = {
    val resultList = new ListBuffer[Result]

    // Check for a valid DateType
    val v1 = new Nbnv78Validator
    val r1 = v1.validate(record)
    resultList.append(r1)

    // Ensure any supplied dates are valid
    val v2 = new Nbnv68Validator
    val r2 = v2.validate(record)
    resultList.appendAll(r2)

    if (resultList.find(r => r.level == ResultLevel.ERROR).isEmpty) {
      // Check if any of the supplied dates are in the future
      val v3 = new Nbnv71Validator
      val r3 = v3.validate(record)
      resultList.appendAll(r3)

      // The start date should not be before the end date, can however be the same
      val v4 = new Nbnv72Validator
      val r4 = v4.validate(record)
      resultList.append(r4)
    }

    if (resultList.find(r => r.level == ResultLevel.ERROR).isEmpty) {
      // We only should every carry on if we have a set of valid dates otherwise these tests make no sense anymore
      val results = validateAccordingToType(record)
      resultList.appendAll(results)
    }

    resultList
  }

  def validateAccordingToType(record: NbnRecord): List[Result] =  record.dateType match {
    case "<D" => (new Nbnv194Validator).validate(record)
    case "D" => (new Nbnv73Validator).validate(record)
    case ">D" => (new Nbnv217Validator).validate(record)
    case "<Y" | "-Y" => (new Nbnv76Validator).validate(record)
    case "Y-" | ">Y"  => (new Nbnv75Validator).validate(record)
    case "Y"  => (new Nbnv284Validator).validate(record)
    case "DD" => (new Nbnv195Validator).validate(record)
    case "O" | "M"=> (new Nbnv74Validator).validate(record)
    case "P" | "OO" | "MM" => (new Nbnv196Validator).validate(record)
    case "ND" | "U" => (new Nbnv77Validator).validate(record)
    case "YY" => (new Nbnv197Validator).validate(record)
  }
}
