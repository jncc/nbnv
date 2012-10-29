package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}
import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.terms.DwcTerm
import uk.org.nbn.nbnv.importer.records.NbnRecord

class Nbnv62Validator {

  def validate(record: NbnRecord) = {
    val validator = new LengthValidator()
    validator.validate(record.key,"SurveyKey", record.surveyKey getOrElse "",30)
  }
}
