package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.terms.DwcTerm
import collection.mutable
import collection.immutable.TreeSet

//todo: Requires test suite
//class Nbnv61Validator extends AggregateValidator {
class Nbnv61Validator {

  var keyTree = new TreeSet[String]()


  def processRecord(record: NbnRecord) = {

    val code = "NBNV-61"
    val key = record.key.toUpperCase

    //duplicates NBNV-55
    if (key == None) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: The RecordKey field is required".format(code)
        def reference = key
      }
    } else if (keyTree.contains(key)) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: The RecordKey, %s already occurs in this dataset. Each recordkey must be unique".format(code,key)
        def reference = key
      }
    }
    else {
      keyTree = keyTree.insert(key)

      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: Record key '%s' is unique so far.".format(code,key)
        def reference = key
      }
    }
  }

  def  notifyComplete() {}
}

