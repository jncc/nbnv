package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv840Validator {
  val code = "NBNV-840"

  def validate(record: NbnRecord) = {
    if (record.eventDateRaw.isDefined) {
      record.dateTypeRaw match {
        case None => new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
          def reference: String = record.key
          def message: String = "%s: The date type has not been supplied, it will be default to date type 'D' for a single event date".format(code)
        }
        case Some("D") => new Result {
            def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
            def reference: String = record.key
            def message: String = "%s: Validated: The date type 'D' has been supplied for a single event date".format(code)
          }
        case _ => new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.ERROR
          def reference: String = record.key
          def message: String = "%s: The DateType, %s is not correct for the provided date".format(code, record.dateType)
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
