package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.archive.ArchiveMetadata

class Nbnv58ValidatorSuite extends BaseFunSuite {
  test("Nbnv58 should validate if a grid reference is specified") {
    val metadata = mock[ArchiveMetadata]
    when(metadata.gridReference).thenReturn(Some(1))

    val v = new Nbnv58Validator
    val r = v.validate(metadata)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv58 should validate if an easting, northing and srs is specified") {
    val metadata = mock[ArchiveMetadata]
    when(metadata.gridReference).thenReturn(None)
    when(metadata.east).thenReturn(Some(1))
    when(metadata.north).thenReturn(Some(2))
    when(metadata.srs).thenReturn(Some(3))

    val v = new Nbnv58Validator
    val r = v.validate(metadata)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv58 should validate if an NBN feature key is specified") {
    val metadata = mock[ArchiveMetadata]
    when(metadata.gridReference).thenReturn(None)
    when(metadata.east).thenReturn(None)
    when(metadata.north).thenReturn(None)
    when(metadata.srs).thenReturn(None)
    when(metadata.featureKey).thenReturn(Some(1))

    val v = new Nbnv58Validator
    val r = v.validate(metadata)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv58 should not validate if an no location columns are identified") {
    val metadata = mock[ArchiveMetadata]
    when(metadata.gridReference).thenReturn(None)
    when(metadata.east).thenReturn(None)
    when(metadata.north).thenReturn(None)
    when(metadata.srs).thenReturn(None)
    when(metadata.featureKey).thenReturn(None)

    val v = new Nbnv58Validator
    val r = v.validate(metadata)

    r.level should be (ResultLevel.ERROR)
  }
}
