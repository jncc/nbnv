package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._

/**
 * Created by felix mason on 30/06/2014.
 */
class Nbnv763ValidatorSuite extends BaseFunSuite{
  val metadata = mock[Metadata]
  val repo = mock[Repository]

  test("should validate an existing organisation name") {
    val orgName = "GoodOrgName"

    when(metadata.datasetProviderName).thenReturn(orgName)
    when(repo.confirmOrganisationExits(orgName)).thenReturn(true)

    val v = new Nbnv763Validator(repo)
    var r = v.validate(metadata)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate an organisation name that does not exist") {
    val orgName = "BadOrgName"

    when(metadata.datasetProviderName).thenReturn(orgName)
    when(repo.confirmOrganisationExits(orgName)).thenReturn(false)

    val v = new Nbnv763Validator(repo)
    var r = v.validate(metadata)

    r.level should be (ResultLevel.ERROR)
  }
}
