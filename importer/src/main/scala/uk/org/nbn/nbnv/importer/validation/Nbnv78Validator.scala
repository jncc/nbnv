package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.records.NbnRecord

class Nbnv78Validator {

  def code = "NBNV-78"

  // Record DateType must be one of the following
  def validate(record: NbnRecord) = record.dateType match {
    case Some("D") | Some("DD") | Some("O") | Some("OO") | Some("Y") | Some("-Y") | Some("YY") | Some("ND") | Some("U") => {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "%s: Validated: Found a valid date type".format(code)
      }
    }
    case None => {
      //nbnv-935 - Single date field does not require
      if (record.eventDateRaw.isDefined) {
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
          def reference: String = record.key
          def message: String = "%s: Validated: A date type is not required if only an event date is specified".format(code)
        }
      } else {
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: A vague date type must be specified".format(code)
        }
      }
    }
    case _ => {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: Unrecognised vague date type '%s'".format(code, record.dateType)
      }
    }
  }
}
