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
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: A DateType is required for this record".format(code)
      }
    }
    case _ => {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: The DateType, %s is not one of the recognised types used in the NBN Exchange Format".format(code, record.dateType)
      }
    }
  }
}
