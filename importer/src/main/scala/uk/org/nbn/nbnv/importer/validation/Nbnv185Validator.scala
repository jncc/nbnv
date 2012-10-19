package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv185Validator {
  def validate(record: NbnRecord) = {
    val srs = record.srsRaw

    srs match {
      case "27700" | "29903" | "23030" | "4326" => {
        new Result {
          def level = ResultLevel.DEBUG
          def message = "Validated: A recognised SRS was provided"
          def reference = "Record " + record.key
        }
      }
      case _ => {
        new Result {
          def level = ResultLevel.ERROR
          def message = "The spatial reference system '%s' is not recognised".format(srs)
          def reference = "Record " + record.key
        }
      }
    }
  }
}
