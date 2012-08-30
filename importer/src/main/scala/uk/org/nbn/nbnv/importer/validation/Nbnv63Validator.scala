package uk.org.nbn.nbnv.importer.validation

import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.terms.DwcTerm
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.records.NbnRecord

class Nbnv63Validator {
  def validate(record: NbnRecord) = {

    if (record.sampleKey == null || record.sampleKey.length <= 30) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: SampleKey - max field length 30 characters."
        def reference = "Record " + record.key
      }
    } else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "SampleKey must not be more than 30 characters."
        def reference = "Record " + record.key
      }
    }
  }
}
