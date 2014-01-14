package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.data.Repository
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv64ValidatorSuite extends BaseFunSuite {

  test("should validate know tvk") {
    val repo = mock[Repository]
    when(repo.confirmTaxonVersionKey("some tvk")).thenReturn(true)

    val record = mock[NbnRecord]
    when(record.taxonVersionKey).thenReturn(Some("some tvk"))

    val v = new Nbnv64Validator(repo)
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate unknown tvk") {
    val repo = mock[Repository]
    when(repo.confirmTaxonVersionKey("some tvk")).thenReturn(false)

    val record = mock[NbnRecord]
    when(record.taxonVersionKey).thenReturn(Some("some tvk"))

    val v = new Nbnv64Validator(repo)
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate when no tvk is provided") {
    val repo = mock[Repository]

    val record = mock[NbnRecord]
    when(record.taxonVersionKey).thenReturn(None)

    val v = new Nbnv64Validator(repo)
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
