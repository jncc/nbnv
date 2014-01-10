package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.archive.ArchiveMetadata

class Nbnv57ValidatorSuite extends BaseFunSuite  {

  test("should validate if only a date is specified") {
    val metadata = mock[ArchiveMetadata]
    when(metadata.date).thenReturn(Some(3))
    when(metadata.startDate).thenReturn(None)
    when(metadata.endDate).thenReturn(None)
    when(metadata.dateType).thenReturn(None)

    val v = new Nbnv57Validator
    val r = v.validate(metadata)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate when a start date and date type are specified") {
    val metadata = mock[ArchiveMetadata]
    when(metadata.startDate).thenReturn(Some(3))
    when(metadata.dateType).thenReturn(Some(4))
    when(metadata.endDate).thenReturn(None)
    when(metadata.date).thenReturn(None)

    val v = new Nbnv57Validator
    val r = v.validate(metadata)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate when an end date and date type are specified") {
    val metadata = mock[ArchiveMetadata]
    when(metadata.startDate).thenReturn(None)
    when(metadata.endDate).thenReturn(Some(2))
    when(metadata.dateType).thenReturn(Some(4))
    when(metadata.date).thenReturn(None)

    val v = new Nbnv57Validator
    val r = v.validate(metadata)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate if the all nbn eventDate field extensions are specified") {
    val metadata = mock[ArchiveMetadata]
    when(metadata.startDate).thenReturn(Some(1))
    when(metadata.endDate).thenReturn(Some(2))
    when(metadata.dateType).thenReturn(Some(3))
    when(metadata.date).thenReturn(None)

    val v = new Nbnv57Validator
    val r = v.validate(metadata)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate if no eventDate field is specified") {
    val metadata = mock[ArchiveMetadata]
    when(metadata.startDate).thenReturn(None)
    when(metadata.endDate).thenReturn(None)
    when(metadata.dateType).thenReturn(None)
    when(metadata.date).thenReturn(None)

    val v = new Nbnv57Validator
    val r = v.validate(metadata)

    r.level should be (ResultLevel.ERROR)
  }
}
