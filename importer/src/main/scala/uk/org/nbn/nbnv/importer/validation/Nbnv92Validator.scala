package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv92Validator {
  def validate(record: NbnRecord) = {
    if (record.determiner == null || record.determiner.length <= 140){
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Determiner name is less then or equal to 140 characters"
        def reference = "Record " + record.key
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "Determiner name is greater then 140 characters"
        def reference = "Record " + record.key
      }
    }
  }
}
