package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.terms.DwcTerm
import collection.mutable
import collection.immutable.TreeSet


//class Nbnv61Validator extends AggregateValidator {
class Nbnv61Validator {

  var keyTree = new TreeSet[String]()


  def processRecord(record: StarRecord) = {
    val key = record.core.value(DwcTerm.occurrenceID)

    //duplicates NBNV-55
    if (key == "") {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-61: Key is blank"
        def reference = key
      }
    } else if (keyTree.contains(key)) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-61: Duplicate record key. Key '%s' is not unique".format(key)
        def reference = key
      }
    }
    else {
      keyTree = keyTree.insert(key)

      new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-61: Validated: Record key '%s' is unique so far.".format(key)
        def reference = key
      }
    }
  }

  def  notifyComplete() {}
}

