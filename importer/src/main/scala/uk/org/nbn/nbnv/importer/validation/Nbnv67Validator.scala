package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.records.NbnRecord

//validate sensitive flag
class Nbnv67Validator {

  def validate(record: NbnRecord) = record.sensitiveOccurrenceRaw map { _.toLowerCase } match {

    case None | Some("true") | Some("false") =>
      new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-67: Validated: sensitive field is valid"
        def reference = record.key
      }
    case _ => {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-67: The optional sensitive field must be 'true' or 'false' if defined"
        def reference = record.key
      }
    }
  }
}
