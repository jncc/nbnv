package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._


class KeyGeneratorSuite extends BaseFunSuite {

  test("when there are no existing datasets, should generate GA000001") {
    val r = mock[TaxonDatasetRepository]
    when(r.getLatestTaxonDatasetKey).thenReturn(None)

    val g = new KeyGenerator(r)
    val result = g.nextTaxonDatasetKey
    result should be ("GA000001")
  }

  
}
