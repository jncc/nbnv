package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.records.NbnRecord

//validate sensitive flag
class Nbnv67Validator {
  val code = "NBNV-67"

  def validate(record: NbnRecord) = record.sensitiveOccurrenceRaw map { _.toLowerCase } match {

    case None | Some("true") | Some("false") =>
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: sensitive field is valid".format(code)
        def reference = record.key
      }
    case _ => {
      new Result {
        def level = ResultLevel.ERROR
        //Error message reflects the original NBN data values provided for this field and not the DwC values
        //assigned by the first stage importer.
        def message = "%s: A Sensitive value of 'T' or 'F' is required for this record".format(code)
        def reference = record.key
      }
    }
  }
}
