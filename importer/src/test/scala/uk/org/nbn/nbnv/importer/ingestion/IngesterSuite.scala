package uk.org.nbn.nbnv.importer.ingestion

import org.mockito.Mockito._
import javax.persistence.{EntityTransaction, EntityManager}
import uk.org.nbn.nbnv.metadata.Metadata
import org.gbif.dwc.text.{StarRecord, Archive}
import org.gbif.utils.file.ClosableIterator
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.Options
import org.apache.log4j.Logger

class IngesterSuite extends BaseFunSuite {

  trait ArrangeAndAct {

    // arrange
    val options = mock[Options]

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
    val ingester = new Ingester(options, mock[Logger], em, datasetIngester, recordIngester)
    ingester.ingest(archive, metadata)
  }

  test("should begin transaction") {
    new ArrangeAndAct {
      // assert
      verify(t).begin()
    }
  }

  test("should commit transaction") {
    new ArrangeAndAct {
      // assert
      verify(t).commit()
    }
  }
}
