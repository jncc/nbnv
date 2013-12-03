package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv840Validator {
  val code = "NBNV-840"

  def validate(record: NbnRecord) = {
    if (record.eventDateRaw.isDefined) {
      record.dateType match {
        case null => new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: A date type of 'D' must be supplied for event dates".format(code)
        }
        case "D" => new Result {
            def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
            def reference: String = record.key
            def message: String = "%s: Validated: The date type 'D' has been supplied for a singel event date".format(code)
          }
        case _ => new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The date type '%s' is not valid for single event dates".format(code, record.dateType)
        }
      }
    } else {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "%s: Validated: Not applicable to records that do not have an event date".format(code)
      }
    }
  }

}
