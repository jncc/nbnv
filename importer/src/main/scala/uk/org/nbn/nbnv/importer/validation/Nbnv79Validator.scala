package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}

class Nbnv79Validator {
  def validate(record: NbnRecord) = {
    if (record.siteKey == null || record.siteKey.length <= 30){
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Site key does not exceed maximum length"
        def reference = "Record " + record.key
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "Site key exceeds maximum length of 30 characters"
        def reference = "Record " + record.key
      }
    }
  }
}
