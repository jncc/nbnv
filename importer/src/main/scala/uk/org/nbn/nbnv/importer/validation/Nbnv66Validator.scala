package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.BadDataException

//validate Absence flag
class Nbnv66Validator {
  val code = "NBNV-66"

  def validate(record: NbnRecord) = record.absenceRaw map { _.toLowerCase } match {

    case None | Some("presence") | Some ("absence") => {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: Absence is null, presence or absence".format(code)
        def reference = record.key
      }
    }
    case _ => {
      new Result {
        def level = ResultLevel.ERROR
        //NB Error message relates to the original data soured from the NBN format and not the DwC occurance status
        //controlled vocab. - Error message will be returned to end user. Translation of NBN term to DwC occurs in
        //first stage importer.
        def message = "%s: A ZeroAbundance value of 'T' or 'F' is required for this record".format(code)
        def reference = record.key
      }
    }
  }
}
