package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.data.Repository
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv600ValidatorSuite extends BaseFunSuite {
  val metadata = mock[Metadata]
  val repo = mock[Repository]


  test("should ignore existing datasets") {
    when(metadata.datasetKey).thenReturn("ExistingKey")

    val v = new Nbnv600Validator(repo)
    val r = v.validate(metadata)

    r.level should  be (ResultLevel.DEBUG)
  }

  test("should validate a vaild email for a new dataset identified by a null key") {
    val validEmail = "valid.email@example.com"
    when(metadata.administratorEmail).thenReturn(validEmail)
    when(repo.confirmUserExistsByEamil(validEmail)).thenReturn(true)

    val v = new Nbnv600Validator(repo)
    val r = v.validate(metadata)

    r.level should  be (ResultLevel.DEBUG)
  }

  test("should validate a vaild email for a new dataset identified by an empty key") {
    val validEmail = "valid.email@example.com"
    when(metadata.datasetKey).thenReturn("")
    when(metadata.administratorEmail).thenReturn(validEmail)
    when(repo.confirmUserExistsByEamil(validEmail)).thenReturn(true)

    val v = new Nbnv600Validator(repo)
    val r = v.validate(metadata)

    r.level should  be (ResultLevel.DEBUG)
  }

  test("should not validate a vaild email for a new dataset identified by a null key") {
    val invalidEmail = "invalid.email@example.com"
    when(metadata.administratorEmail).thenReturn(invalidEmail)
    when(repo.confirmUserExistsByEamil(invalidEmail)).thenReturn(false)

    val v = new Nbnv600Validator(repo)
    val r = v.validate(metadata)

    r.level should  be (ResultLevel.ERROR)
  }

  test("should not validate a vaild email for a new dataset identified by an empty key") {
    val invalidEmail = "invalid.email@example.com"
    when(metadata.datasetKey).thenReturn("")
    when(metadata.administratorEmail).thenReturn(invalidEmail)
    when(repo.confirmUserExistsByEamil(invalidEmail)).thenReturn(false)

    val v = new Nbnv600Validator(repo)
    val r = v.validate(metadata)

    r.level should  be (ResultLevel.ERROR)
  }
}
