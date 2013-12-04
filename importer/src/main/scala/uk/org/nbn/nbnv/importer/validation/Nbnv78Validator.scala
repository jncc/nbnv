package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv78Validator {

  // Record DateType must be one of the following
  def validate(record: NbnRecord) = record.dateType match {
    case "D" | "DD" | "O" | "OO" | "Y" | "-Y" | "YY" | "ND" | "U" => {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "NBNV-78: Validated: Found a valid date type"
      }
    }
    case null => {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "NBNV-78: A vague date type must be specified"
      }
    }
    case _ => {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "NBNV-78: Unrecognised vague date type '%s'".format(record.dateType)
      }
    }
  }
}
