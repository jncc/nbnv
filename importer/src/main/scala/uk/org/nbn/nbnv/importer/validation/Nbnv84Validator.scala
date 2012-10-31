package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.records.NbnRecord

class Nbnv84Validator {
  def validate(record: NbnRecord) = {
    if ((record.eastRaw.get matches """^\-*\d{1,3}(\.\d+)*$""") == false) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-84: The value of east is not a valid numeric longitude: %s".format(record.eastRaw.get)
        def reference = record.key
      }
    }
    else if ((record.northRaw.get matches """^\-*\d{1,2}(\.\d+)*$""") == false) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-84: The value of north is not a valid numeric latitude: %s".format(record.northRaw.get)
        def reference = record.key
      }
    }
    else  {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-84: Validated: east and north are numeric"
        def reference = record.key
      }
    }
  }
}
