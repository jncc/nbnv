package uk.org.nbn.nbnv.importer.validation

import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.archive.ArchiveMetadata

class Nbnv56ValidatorSuite extends BaseFunSuite{
  test("Nbnv56 should not validate if taxonVersionKey is null") {
    val metadata = mock[ArchiveMetadata]
    when(metadata.taxonVersionKey).thenReturn(None)

    val v = new Nbnv56Validator
    val r = v.validate(metadata)

    r.level should be (ResultLevel.ERROR)
  }

  test("Nbnv56 should validate if taxonVersionKey is not null") {
    val metadata = mock[ArchiveMetadata]
    when(metadata.taxonVersionKey).thenReturn(Some(2))

    val v = new Nbnv56Validator
    val r = v.validate(metadata)

    r.level should be (ResultLevel.DEBUG)
  }
}
