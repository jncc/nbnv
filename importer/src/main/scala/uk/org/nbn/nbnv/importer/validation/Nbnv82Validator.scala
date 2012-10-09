package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv82Validator {

  def validate(record: NbnRecord) = {
    def success = {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: A spatial reference has been supplied"
        def reference = "Record " + record.key
      }
    }

    if (record.gridReference.isDefined) {
      success
    }
    else if (record.featureKey.isDefined) {
      success
    }
    else if (record.east.isDefined && record.north.isDefined && record.srs.isDefined) {
      success
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "A spatial reference must be supplied"
        def reference = "Record " + record.key
      }
    }
  }
}
