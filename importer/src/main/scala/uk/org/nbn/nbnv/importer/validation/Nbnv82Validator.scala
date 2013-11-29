package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv82Validator {
  val code = "NBNV-82"

  def validate(record: NbnRecord) = {
    var count = 0

    if (record.gridReferenceRaw.isDefined) {
      count += 1
    }

    if (record.featureKey.isDefined) {
      count += 1
    }

    if (record.eastRaw.isDefined && record.northRaw.isDefined && (record.srsRaw.isDefined || record.gridReferenceTypeRaw.isDefined)) {
      count += 1
    }

    if (count > 1) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: More than one definition of a spatial reference has been supplied".format(code)
        def reference = record.key
      }
    }
    else if (count < 1) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: At least one definition of a spatial reference must be supplied".format(code)
        def reference = record.key
      }
    }
    else {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: Only one spatial reference definistion has been supplied".format(code)
        def reference = record.key
      }
    }
  }
}
