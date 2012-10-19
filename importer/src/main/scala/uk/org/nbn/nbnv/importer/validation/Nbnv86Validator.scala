package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv86Validator {
  def validate(record: NbnRecord) = {
    //todo figure out some valid test for lat long extent
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
      def reference: String = record.key
      def message: String = "Validated: Nbnv86 not implemented"
    }
  }
}
