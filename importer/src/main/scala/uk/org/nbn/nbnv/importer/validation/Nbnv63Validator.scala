package uk.org.nbn.nbnv.importer.validation

import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.terms.DwcTerm
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.records.NbnRecord

//validate SampleKey length
class Nbnv63Validator {
  def validate(record: NbnRecord) = {
    val validator = new LengthValidator
    validator.validate("NBNV-63", record.key, "SampleKey", record.sampleKey getOrElse "", 30)
  }
}
