package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

/**
 * Created by felix mason on 30/06/2014.
 */
class Nbnv763Validator(repo: Repository) {

  def validate(metadata: Metadata): Result = {
    val code = "NBNV-763"

    if (repo.confirmOrganisationExits(metadata.datasetProviderName)) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: The organisation '%s' exits".format(code, metadata.datasetProviderName)
        def reference = metadata.datasetKey
      }
    } else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: The organisation name, %s is not on the NBN Gateway. The organisation must be registered on the NBN Gateway first".format(code, metadata.datasetProviderName)
        def reference = metadata.datasetKey
      }
    }
  }
}
