package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}
import uk.org.nbn.nbnv.importer.records.NbnRecord

/**
 * Created with IntelliJ IDEA.
 * User: Matt Debont
 * Date: 15/10/12
 * Time: 10:51
 * To change this template use File | Settings | File Templates.
 */
class Nbnv163Validator {
  def validate(record:NbnRecord) = {

    def success = {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "RecordKey was present"
        def reference = "Record " + record.key
      }
    }

    def fail = {
      new Result {
        def level = ResultLevel.ERROR
        def message = "RecordKey was not present"
        def reference = ""
      }
    }

    if (record.key.trim != "")
      success else fail
  }
}
