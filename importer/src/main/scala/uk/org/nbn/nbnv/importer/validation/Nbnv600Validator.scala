package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv600Validator(repo: Repository) {
  def validate(metadata: Metadata) : Result = {
    val code = "NBNV-600"

    if (metadata.datasetKey != null && ! metadata.datasetKey.isEmpty) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: This is an existing dataset".format(code)
        def reference = "metadata"
      }
    }
    else if ((metadata.datasetKey == null || metadata.datasetKey.isEmpty)
      && (metadata.administratorEmail == null || metadata.administratorEmail.isEmpty)) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: A dataset administrator email address for a registered user is required for this new dataset".format(code)
        def reference = "metadata"
      }
    }
    else if ((metadata.datasetKey == null || metadata.datasetKey.isEmpty) && repo.confirmUserExistsByEamil(metadata.administratorEmail)) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: This is a new dataset and the administrator exists".format(code)
        def reference = "metadata"
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: The dataset administrator email address, '%s' is not on the NBN Gateway"
          .format(code, metadata.administratorEmail)
        def reference = "metadata"
      }
    }
  }


}
