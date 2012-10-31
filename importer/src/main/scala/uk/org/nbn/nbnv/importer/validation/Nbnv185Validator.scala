package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv185Validator {
  def validate(record: NbnRecord) = {
    val srs = record.srsRaw

    srs match {
      case Some("27700") | Some("29903") | Some("23030") | Some("4326") => {
        new Result {
          def level = ResultLevel.DEBUG
          def message = "Validated: A recognised SRS was provided"
          def reference = record.key
        }
      }
      case None => {
        new Result {
          def level = ResultLevel.ERROR
          def message = "The spatial reference system must be specified"
          def reference = record.key
        }
      }
      case _ => {
        new Result {
          def level = ResultLevel.ERROR
          def message = "The spatial reference system '%s' is not recognised".format(srs)
          def reference = record.key
        }
      }
    }
  }
}
