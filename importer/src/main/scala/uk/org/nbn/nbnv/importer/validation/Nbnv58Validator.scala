package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv58Validator {
  def validate(record: NbnRecord) = {
    if (record.gridReference.isDefined
      || (record.east.isDefined && record.north.isDefined && record.srs.isDefined)
      || record.featureKey.isDefined){

      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: An acceptable combination of location fields has been specified."
        def reference = record.key
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "A record must have a location defined for it. An acceptable combination of location fields has not been specified."
        def reference = record.key
      }
    }
  }
}
