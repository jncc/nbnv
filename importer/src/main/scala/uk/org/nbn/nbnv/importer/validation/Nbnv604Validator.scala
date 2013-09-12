package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv604Validator (repo: Repository) {
  def validate(metadata: Metadata) = {
    if (metadata.datasetKey == null || metadata.datasetKey.isEmpty) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-604: This is a new dataset"
        def reference = ""
      }
    }
    else if (! repo.confirmDatasetExists(metadata.datasetKey) ) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-604: The dataset key %s does not exist".format(metadata.datasetKey)
        def reference = metadata.datasetKey
      }
    }
    else {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-604: The dataset key %s exists".format(metadata.datasetKey)
        def reference = metadata.datasetKey
      }
    }
  }

}
