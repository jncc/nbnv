package uk.org.nbn.nbnv.importer.data

import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import javax.persistence.{EntityTransaction, EntityManager}
import uk.org.nbn.nbnv.jpa.nbncore.{Dataset, TaxonDataset}
import uk.org.nbn.nbnv.metadata.Metadata
import org.gbif.dwc.text.{StarRecord, Archive}
import org.gbif.utils.file.ClosableIterator

@RunWith(classOf[JUnitRunner])
class IngesterSuite extends FunSuite with ShouldMatchers with MockitoSugar {

  trait ArrangeAndAct {

    // arrange
    val t = mock[EntityTransaction]

    val em = mock[EntityManager]
    when(em.getTransaction).thenReturn(t)

    val datasetIngester = mock[DatasetIngester]
    val recordIngester = mock[RecordIngester]

    val archive = mock[Archive]
    val iterator = mock[ClosableIterator[StarRecord]]
    when(archive.iteratorRaw).thenReturn(iterator)

    val metadata = mock[Metadata]

    // act
    val ingester = new Ingester(em, datasetIngester, recordIngester)
    ingester.ingest(archive, metadata)
  }

  test("should begin transaction") {
    new ArrangeAndAct {
      verify(t).begin()
    }
  }
}
