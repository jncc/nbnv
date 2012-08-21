package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.fidelity.{Result, ResultLevel}
import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.terms.DwcTerm


class Nbnv62Validator {

  def validate(record: StarRecord) = {

    val surveyKey = record.core.value(DwcTerm.collectionCode)

    if (surveyKey.length > 30) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "SurveyKey must not be more than 30 characters."
        def reference = "Record " + record.core.value(DwcTerm.occurrenceID)
      }
    } else {
      new Result {
        def level = ResultLevel.INFO
        def message = "Validated: SurveyKey - max field length 30 characters."
        def reference = "Record " + record.core.value(DwcTerm.occurrenceID)
      }
    }
  }
}
