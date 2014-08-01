package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}


class Nbnv951Validator {
  val code = "NBNV-951"

  def validate(metadata: Metadata) = {
    if (metadata.datasetTitle == null || metadata.datasetTitle == "") {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: A title for this dataset is required".format(code)
        def reference = "metadata"
      }
    } else {
      val validator = new LengthValidator()
      validator.validate(code, metadata.datasetTitle, "The dataset title", metadata.datasetTitle, 200)
    }
  }
}
