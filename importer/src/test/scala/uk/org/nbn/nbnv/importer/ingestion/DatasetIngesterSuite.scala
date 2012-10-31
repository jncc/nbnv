package uk.org.nbn.nbnv.importer.ingestion

import org.mockito.Mockito._
import org.mockito.Matchers._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.jpa.nbncore.{Dataset, TaxonDataset}
import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.data.{Database, Repository, KeyGenerator}
import org.mockito.Mockito
import org.apache.log4j.Logger

class DatasetIngesterSuite extends BaseFunSuite {

  ignore("an existing dataset should be updated") {

    // arrange
    val key = "existing-dataset-key"

    val metadata = mock[Metadata]
    when(metadata.datasetKey) thenReturn key

    val dataset = mock[Dataset]
    val taxonDataset = mock[TaxonDataset]
    when(taxonDataset.getDataset).thenReturn(dataset)

    val em = mock[EntityManager]
    when(em.find(classOf[TaxonDataset], key)).thenReturn(taxonDataset)

    val keyGenerator = mock[KeyGenerator]
    val db = mock[Database]

    // act
    val ingester = new DatasetIngester(mock[Logger], db, keyGenerator)
    val result = ingester.upsertDataset(metadata)

    // assert - that the entity manager was not called with the retrieved dataset
    verify(em, never()).persist(dataset)
    // assert - that a property was set
    result.getDatasetKey should be (metadata.datasetKey)
  }

  ignore("a new dataset should be inserted") {

    // arrange
    val key = "new-dataset-key"
    val metadata = mock[Metadata]
    when(metadata.datasetKey) thenReturn key

    val em = mock[EntityManager]
    when(em.find(classOf[TaxonDataset], key)).thenReturn(null)

    val dataset = mock[Dataset]
    when(em.merge(any(classOf[Dataset]))).thenReturn(dataset)

    val keyGenerator = mock[KeyGenerator]
    val db = mock[Database]

    // act
    val ingester = new DatasetIngester(mock[Logger], db, keyGenerator)
    val taxonDataset = ingester.upsertDataset(metadata)

    //verify that setDataset is called against the new taxondataset enity with a dataset
    // check that the taxondataset has got a dataset on it
    //verify that em.persist is called with a new taxon dataset.


    // assert - that the entity manager was called with a dataset
    verify(em).persist(any(classOf[TaxonDataset])) // would be better to verify that it's called with some dataset with key=key
  }
}
