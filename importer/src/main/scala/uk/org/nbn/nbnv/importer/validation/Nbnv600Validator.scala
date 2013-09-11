package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv600Validator(repo: Repository) {
  def validate(metadata: Metadata) : Result = {
    if (! metadata.datasetKey.isEmpty) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-600: This is an existing dataset"
        def reference = metadata.datasetKey
      }
    }
    else if (metadata.datasetKey.isEmpty && repo.confirmUserExistsByEamil(metadata.administratorEmail))
    {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-600: This is a new dataset and the administrator exists"
        def reference = metadata.datasetKey
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-600: The user %s %s (%s) does not exist and cannot be set as the dataset administrator"
          .format(metadata.administratorForename, metadata.administratorSurname, metadata.administratorSurname)
        def reference = metadata.datasetKey
      }
    }
  }


}
