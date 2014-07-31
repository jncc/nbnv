package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv604Validator (repo: Repository) {
  def validate(metadata: Metadata) = {
    val code = "NBNV-604"

    if (metadata.datasetKey == null || metadata.datasetKey.isEmpty) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: This is a new dataset".format(code)
        def reference = "metadata"
      }
    }
    else if (! repo.confirmDatasetExists(metadata.datasetKey) ) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: The supplied datasetkey, '%s' is not on the NBN Gateway".format(code, metadata.datasetKey)
        def reference = "metadata"
      }
    }
    else {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: The dataset key %s exists".format(code, metadata.datasetKey)
        def reference = "netadata"
      }
    }
  }

}
