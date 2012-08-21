package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._


class KeyGeneratorSuite extends BaseFunSuite {

  test("when there are no existing datasets, should generate GA000001") {

    val r = mock[Repository]
    when(r.getLatestDatasetKey).thenReturn(None)

    val g = new KeyGenerator(r)
    val result = g.nextTaxonDatasetKey
    result should be ("GA000001")
  }

  test("when there are 3 existing datasets, should generate GA000004") {

    val r = mock[Repository]
    when(r.getLatestDatasetKey).thenReturn(Some("GA000003"))

    val g = new KeyGenerator(r)
    val result = g.nextTaxonDatasetKey
    result should be ("GA000004")
  }

  test("when there are 9 existing datasets, should generate GA000010") {

    val r = mock[Repository]
    when(r.getLatestDatasetKey).thenReturn(Some("GA000009"))

    val g = new KeyGenerator(r)
    val result = g.nextTaxonDatasetKey
    result should be ("GA000010")
  }

  test("when there are 1150 existing datasets, should generate GA001151") {

    val r = mock[Repository]
    when(r.getLatestDatasetKey).thenReturn(Some("GA001150"))

    val g = new KeyGenerator(r)
    val result = g.nextTaxonDatasetKey
    result should be ("GA001151")
  }
}
