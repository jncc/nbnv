package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.terms.DwcTerm


//class Nbnv61Validator extends AggregateValidator {
class Nbnv61Validator {

  var keys = List[String]()

  //have to use the starrecord becasue of NBNV-233
  def processRecord(record: StarRecord) = {

    val key = record.core.value(DwcTerm.occurrenceID)

    val all = key :: keys

    if (all.distinct.size != all.size) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-61: Duplicate record key. Key '%s' is not unique".format(key)
        def reference = key
      }
    }
    else {
      // remember this key for next time we are called
      keys = all

      new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-61: Validated: Record key '%s' is unique so far.".format(key)
        def reference = key
      }
    }
  }

  def notifyComplete() {}
}