package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}


class Nbnv951Validator {
  val code = "NBNV-951"

  def validate(metadata: Metadata) = {
    if (metadata.datasetTitle == null || metadata.datasetTitle == "") {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: A dataset title must be provided".format(code)
        def reference = "Metadata"
      }
    } else {
      val validator = new LengthValidator()
      validator.validate(code, metadata.datasetTitle, "DatasetTitle", metadata.datasetTitle, 200)
    }
  }
}
