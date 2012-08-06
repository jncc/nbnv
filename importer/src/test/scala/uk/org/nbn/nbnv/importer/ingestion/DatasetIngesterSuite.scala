package uk.org.nbn.nbnv.importer.ingestion

import org.mockito.Mockito._
import org.mockito.Matchers._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.jpa.nbncore.{Dataset, TaxonDataset}
import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.data.KeyGenerator

class DatasetIngesterSuite extends BaseFunSuite {

  test("an existing dataset should be updated") {

    // arrange
    val key = "existing-dataset-key"
    val metadata = buildFakeMetadata(key)

    val dataset = mock[Dataset]
    val taxonDataset = mock[TaxonDataset]
    when(taxonDataset.getDataset).thenReturn(dataset)

    val em = mock[EntityManager]
    when(em.find(classOf[TaxonDataset], key)).thenReturn(taxonDataset)

    val keyGenerator = mock[KeyGenerator]

    // act
    val ingester = new DatasetIngester(em, keyGenerator)
    ingester.upsertDataset(metadata)

    // assert - that the entity manager was called with the retrieved dataset
    verify(em).merge(dataset)
  }

  test("a new dataset should be inserted") {

    // arrange
    val key = "new-dataset-key"
    val metadata = buildFakeMetadata(key)
    val em = mock[EntityManager]
    when(em.find(classOf[TaxonDataset], key)).thenReturn(null)
    val dataset = mock[Dataset]
    when(em.merge(any(classOf[Dataset]))).thenReturn(dataset)

    val keyGenerator = mock[KeyGenerator]

    // act
    val ingester = new DatasetIngester(em, keyGenerator)
    val taxonDataset = ingester.upsertDataset(metadata)

    //verify that setDataset is called against the new taxondataset enity with a dataset
    // check that the taxondataset has got a dataset on it
    //verify that em.persist is called with a new taxon dataset.


    // assert - that the entity manager was called with a dataset
    verify(em).persist(any(classOf[TaxonDataset])) // would be better to verify that it's called with some dataset with key=key
  }


  def buildFakeMetadata(key: String) = {
    new Metadata {
      val datasetKey: String = key
      val accessConstraints: String = ""
      val geographicCoverage: String = ""
      val useConstraints: String = ""
      val description: String = ""
      val datasetTitle: String = ""
      val dataCaptureMethod: String = ""
      val dataQuality: String = ""
      val purpose: String = ""
    }
  }
}
