package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv80Validator {
  def validate(record: NbnRecord) = {
    if (record.siteName == null || record.siteName.length <= 100) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Site name is less then 100 characters"
        def reference = "Record " + record.key
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "Site name is less greater then 100 characters"
        def reference = "Record " + record.key
      }
    }


  }
}
