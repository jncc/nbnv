package uk.org.nbn.nbnv.importer.validation

import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class DuplicateSiteKeyValidatorSuite extends BaseFunSuite with BeforeAndAfter{
  
  var record1: NbnRecord = _
  var record2: NbnRecord = _
  var v: DuplicateSiteKeyValidator = _
  
  before {
    record1 = mock[NbnRecord]
    record2 = mock[NbnRecord]
    
    v = new DuplicateSiteKeyValidator
  }

  test("should validate the first record") {
    when(record1.siteKey).thenReturn(Some("siteKey"))
    when(record1.siteName).thenReturn(Some("siteName"))
    
    val r = v.processRecord(record1)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate records with different site keys") {
    when(record1.siteKey).thenReturn(Some("siteKey1"))
    when(record1.siteName).thenReturn(Some("siteName1"))

    when(record2.siteKey).thenReturn(Some("siteKey2"))
    when(record2.siteName).thenReturn(Some("siteName2"))

    val r1 = v.processRecord(record1)

    r1.level should be (ResultLevel.DEBUG)

    val r2 = v.processRecord(record2)

    r2.level should be (ResultLevel.DEBUG)
  }

  test("should validate records with the same site key and site name") {
    val siteKey = Some("siteKey")
    val siteName = Some("siteName")

    when(record1.siteKey).thenReturn(siteKey)
    when(record1.siteName).thenReturn(siteName)

    when(record2.siteKey).thenReturn(siteKey)
    when(record2.siteName).thenReturn(siteName)

    val r1 = v.processRecord(record1)

    r1.level should be (ResultLevel.DEBUG)

    val r2 = v.processRecord(record2)

    r2.level should be (ResultLevel.DEBUG)
  }

  test("should validate records with the site key and no name") {
    val siteKey = Some("siteKey")
    val siteName = None

    when(record1.siteKey).thenReturn(siteKey)
    when(record1.siteName).thenReturn(siteName)

    when(record2.siteKey).thenReturn(siteKey)
    when(record2.siteName).thenReturn(siteName)

    val r1 = v.processRecord(record1)

    r1.level should be (ResultLevel.DEBUG)

    val r2 = v.processRecord(record2)

    r2.level should be (ResultLevel.DEBUG)
  }

  test("should not validate records with the same site key and different names") {
    val siteKey = Some("siteKey")

    when(record1.siteKey).thenReturn(siteKey)
    when(record1.siteName).thenReturn(Some("Name1"))

    when(record2.siteKey).thenReturn(siteKey)
    when(record2.siteName).thenReturn(Some("Name2"))

    val r1 = v.processRecord(record1)

    r1.level should be (ResultLevel.DEBUG)

    val r2 = v.processRecord(record2)

    r2.level should be (ResultLevel.ERROR)
  }
}
