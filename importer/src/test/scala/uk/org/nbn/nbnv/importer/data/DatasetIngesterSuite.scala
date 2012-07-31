package uk.org.nbn.nbnv.importer.data

import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.jpa.nbncore.Dataset
import uk.org.nbn.nbnv.metadata.Metadata

@RunWith(classOf[JUnitRunner])
class DatasetIngesterSuite extends FunSuite with ShouldMatchers with MockitoSugar {

  test("an existing dataset should be updated") {

    // arrange
    val key = "existing-dataset-key"
    val metadata = buildFakeMetadata(key)
    val dataset = mock[Dataset]
    val em = mock[EntityManager]
    when(em.find(classOf[Dataset], key)).thenReturn(dataset)

    // act
    val ingester = new DatasetIngester(em)
    ingester.upsertDataset(metadata)

    // assert - that the entity manager was called with the retrieved dataset
    verify(em).merge(dataset)
  }

  test("a new dataset should be inserted") {

    // arrange
    val key = "new-dataset-key"
    val metadata = buildFakeMetadata(key)
    val em = mock[EntityManager]
    when(em.find(classOf[Dataset], key)).thenReturn(null)

    // act
    val ingester = new DatasetIngester(em)
    ingester.upsertDataset(metadata)

    // assert - that the entity manager was called with a dataset
    verify(em).merge(any(classOf[Dataset])) // would be better to verify that it's called with some dataset with key=key
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
