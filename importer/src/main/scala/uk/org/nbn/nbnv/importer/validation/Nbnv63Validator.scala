package uk.org.nbn.nbnv.importer.validation

import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.terms.DwcTerm
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.records.NbnRecord

class Nbnv63Validator {
  def validate(record: NbnRecord) = {
    val validator = new LengthValidator
    validator.validate(record.key, "SampleKey", record.sampleKey getOrElse "", 30)
  }
}
