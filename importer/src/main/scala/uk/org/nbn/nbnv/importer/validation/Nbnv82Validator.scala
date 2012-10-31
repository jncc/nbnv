package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv82Validator {

  def validate(record: NbnRecord) = {
    var count = 0

    if (record.gridReference.isDefined) {
      count += 1
    }

    if (record.featureKey.isDefined) {
      count += 1
    }

    if (record.eastRaw.isDefined && record.northRaw.isDefined && record.srsRaw.isDefined) {
      count += 1
    }

    if (count > 1) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "More than one definition of a spatial reference has been supplied"
        def reference = record.key
      }
    }
    else if (count < 1) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "At least one definition of a spatial reference must be supplied"
        def reference = record.key
      }
    }
    else {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Only one spatial reference definistion has been supplied"
        def reference = record.key
      }
    }
  }
}
