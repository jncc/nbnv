package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import util.parsing.json.JSON
import uk.org.nbn.nbnv.importer.BadDataException
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}


class Nbnv878Validator {
  def validate(record: NbnRecord) = {
    val code = "NBNV-878"

    if (!record.attributesRaw.isDefined) {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "%s: No JSON attributes are defined".format(code)
      }
    } else {
      try {
        val json = JSON.parseFull(record.attributesRaw.get)
        json.get.asInstanceOf[Map[String, String]]

        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
          def reference: String = record.key
          def message: String = "%s: The JSON attribute list is well formed and parseable".format(code)
        }

      }
      catch {
        case e: Throwable => {
          new Result {
            def level: ResultLevel.ResultLevel = ResultLevel.ERROR
            def reference: String = record.key
            def message: String = "%s: One or more record attributes are incorrectly formatted".format(code)
          }
        }
      }
    }


  }
}
