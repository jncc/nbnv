package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord

class Nbnv55Validator {
    def validate(record: NbnRecord) = {
      val validator = new NullFieldValidator
      validator.validate("NBNV-55", record.key, "RecordKey")
    }

}
