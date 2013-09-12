package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.data.Repository
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv604ValidatorSuite extends BaseFunSuite {
  test("should ignore new datasets with enpty keys") {
    val repo = mock[Repository]
    val metadata = mock[Metadata]

    when(metadata.datasetKey).thenReturn("")

    val v = new Nbnv604Validator(repo)
    val r = v.validate(metadata)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should ignore new datasets with null keys") {
    val repo = mock[Repository]
    val metadata = mock[Metadata]

    when(metadata.datasetKey).thenReturn(null)

    val v = new Nbnv604Validator(repo)
    val r = v.validate(metadata)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate dataset keys that don't exist") {
    val repo = mock[Repository]
    val metadata = mock[Metadata]
    val key = "BadTestKey"

    when(metadata.datasetKey).thenReturn(key)
    when(repo.confirmDatasetExists(key)).thenReturn(false)

    val v = new Nbnv604Validator(repo)
    val r = v.validate(metadata)

    r.level should be (ResultLevel.ERROR)
  }

  test("should validate dataset keys that exist") {
    val repo = mock[Repository]
    val metadata = mock[Metadata]
    val key = "GoodTestKey"

    when(metadata.datasetKey).thenReturn(key)
    when(repo.confirmDatasetExists(key)).thenReturn(true)

    val v = new Nbnv604Validator(repo)
    val r = v.validate(metadata)

    r.level should be (ResultLevel.DEBUG)
  }
}
