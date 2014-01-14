package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.archive.ArchiveMetadata

class Nbnv55ValidatorSuite extends BaseFunSuite {
  test("Nbnv55 should not validate if recordkey is null") {
    val metadata = mock[ArchiveMetadata]
    when(metadata.key).thenReturn(None)

    val v = new Nbnv55Validator
    val r = v.validate(metadata)

    r.level should be (ResultLevel.ERROR)
  }

  test("Nbnv55 should validate if recordkey is not null") {
    val metadata = mock[ArchiveMetadata]
    when(metadata.key).thenReturn(Some(1))

    val v = new Nbnv55Validator
    val r = v.validate(metadata)

    r.level should be (ResultLevel.DEBUG)
  }
}
