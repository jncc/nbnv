package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

/**
 * Created By: Matt Debont
 * Date: 18/10/12
 */
class Nbnv77Validator {

  def validate(record: NbnRecord) = record.dateType match {
    case "ND" => validateDates(record)
    case "U"  => validateDates(record)
    case _    => new Result {def level: ResultLevel.ResultLevel = ResultLevel.ERROR; def reference: String = record.dateType; def message: String = "This validator only supports ND and U records"}
  }

  def validateDates(record: NbnRecord) = {
    if (record.startDate == null && record.endDate == null) {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.dateType
        def message: String = "Found a valid date type and set of dates"
      }
    } else {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.dateType
        def message: String = "Both startdate and enddate must be null"
      }
    }
  }

}
