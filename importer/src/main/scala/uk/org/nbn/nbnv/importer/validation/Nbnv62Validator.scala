package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}
import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.terms.DwcTerm
import uk.org.nbn.nbnv.importer.records.NbnRecord

class Nbnv62Validator {

  def validate(record: NbnRecord) = {

    if (record.surveyKey == null || record.surveyKey.length <= 30) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: SurveyKey - max field length 30 characters."
        def reference = "Record " + record.key
      }
    } else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "SurveyKey must not be more than 30 characters."
        def reference = "Record " + record.key
      }
    }
  }
}
