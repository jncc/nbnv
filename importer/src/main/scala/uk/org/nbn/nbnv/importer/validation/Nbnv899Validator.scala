package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv899Validator {
  val code = "NBNV-899"

  def validate(metadata: Metadata): Result = {
    if (metadata.importType.isDefined) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: The import type has been defined".format(code)
        def reference = "metadata"
      }
    } else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: A valid import type has not been defined".format(code)
        def reference = "metadata"
      }
    }
  }

}
