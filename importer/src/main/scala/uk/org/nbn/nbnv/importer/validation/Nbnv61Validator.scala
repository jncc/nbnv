package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}


class Nbnv61Validator extends AggregateValidator {

  var keys = List[String]()

  def processRecord(record: NbnRecord) = {

    val all = record.key :: keys

    if (all.distinct.size != all.size) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "Duplicate record key. Key '%s' is not unique".format(record.key)
        def reference = record.key
      }
    }
    else {
      // remember this key for next time we are called
      keys = all

      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Record key '%s' is unique so far.".format(record.key)
        def reference = record.key
      }
    }
  }

  def notifyComplete() {

  }
}