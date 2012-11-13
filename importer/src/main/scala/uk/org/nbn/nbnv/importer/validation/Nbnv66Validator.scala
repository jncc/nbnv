package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.BadDataException

//validate Absence flag
class Nbnv66Validator {

  def validate(record: NbnRecord) = record.absenceRaw map { _.toLowerCase } match {

    case None | Some("presence") | Some ("absence") => {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-66: Validated: Absence is null, presence or absence"
        def reference = record.key
      }
    }
    case _ => {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-66: The value of the optional absence field must be either 'presence' or 'absence'"
        def reference = record.key
      }
    }
  }
}
