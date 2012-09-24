package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.metadata.Metadata
import org.mockito.Mockito._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Repository}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.spatial.GridSquareInfoFactory
import uk.org.nbn.nbnv.importer.records.NbnRecord

class FeatureIngesterSuite extends BaseFunSuite {

  test("a new grid square hierarchy should be created") {

    // arrange

    val record = mock[NbnRecord]

    val em = mock[EntityManager]
    //    when(em.find(classOf[TaxonDataset], key)).thenReturn(taxonDataset)

    val repo = mock[Repository]

    val gridSquareInfoFactory = mock[GridSquareInfoFactory]

    // act
    val ingester = new FeatureIngester(em, repo, gridSquareInfoFactory)
    val result = ingester.ensureFeature(record)

//    // assert - that the entity manager was not called with the retrieved dataset
//    verify(em, never()).persist(dataset)
//    // assert - that a property was set
//    result.getDatasetKey should be (metadata.datasetKey)
  }

}
