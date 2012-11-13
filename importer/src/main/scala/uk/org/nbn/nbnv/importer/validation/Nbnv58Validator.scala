package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

//validate a location fields have been defined
class Nbnv58Validator {
  def validate(record: NbnRecord) = {
    if (record.gridReferenceRaw.isDefined
      || (record.east.isDefined && record.north.isDefined && (record.srs.isDefined || record.gridReferenceTypeRaw.isDefined))
      || record.featureKey.isDefined){

      new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-58: Validated: An acceptable combination of location fields has been specified."
        def reference = record.key
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-58: A record must have a location defined for it. An acceptable combination of location fields has not been specified."
        def reference = record.key
      }
    }
  }
}
