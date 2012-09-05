package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv58Validator {
  def validate(record: NbnRecord) = {
    if (record.gridReference != null
      || (record.east != null && record.north != null && record.srs != null)
      || (record.featureKey != null)){

      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: An acceptable combination of location fields has been specified."
        def reference = ""
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "A record must have a location defined for it. An acceptable combination of location fields has not been specified."
        def reference = ""
      }
    }
  }
}
