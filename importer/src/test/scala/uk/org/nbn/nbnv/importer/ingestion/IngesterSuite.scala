package uk.org.nbn.nbnv.importer.ingestion

import org.mockito.Mockito._
import javax.persistence.{EntityTransaction, EntityManager}
import uk.org.nbn.nbnv.importer.metadata.{Mode, Metadata}
import org.gbif.utils.file.ClosableIterator
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.{Target, Options}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.data.{QueryCache, Repository, Database}

import uk.org.nbn.nbnv.importer.reset.DatasetReset
import uk.org.nbn.nbnv.importer.archive.Archive
import uk.org.nbn.nbnv.importer.records.NbnRecord

class IngesterSuite extends BaseFunSuite {

  def fixture = new {

    // arrange
    val options = Options(target = Target.commit)

    val t = mock[EntityTransaction]

    val em = mock[EntityManager]
    when(em.getTransaction).thenReturn(t)

    val datasetIngester = mock[DatasetIngester]
    val recordIngester = mock[RecordIngester]
    val sampleIngester = mock[SampleIngester]
    val surveyIngester = mock[SurveyIngester]
    val siteIngester = mock[SiteIngester]
    val featureIngester = mock[FeatureIngester]
    val recorderIngester = mock[RecorderIngester]
    val repository = mock[Repository]
    val datasetReset = mock[DatasetReset]

    val archive = mock[Archive]
    val iteratorWithIndex = mock[Iterable[(NbnRecord, Int)]]
    val iterator = mock[Iterable[NbnRecord]]
    when(iterator.zipWithIndex).thenReturn(iteratorWithIndex)

    when(archive.records())thenReturn(iterator)

    val metadata = mock[Metadata]
    when(metadata.datasetKey).thenReturn("")
    when(metadata.importType).thenReturn(Some(Mode.upsert))

    val db = new Database(em, mock[Repository], mock[QueryCache])

  }

  test("should begin transaction") {

    val f = fixture

    // act
    val ingester = new Ingester(f.options, mock[Logger], f.db, f.datasetIngester, f.recordIngester, f.surveyIngester, f.sampleIngester, f.siteIngester, f.recorderIngester, f.featureIngester, f.datasetReset)
    ingester.ingest(f.archive, f.metadata)

    // assert

    verify(f.t, times(2)).begin()
  }

  test("should commit transaction") {

    val f = fixture

    // act
    val ingester = new Ingester(f.options, mock[Logger], f.db, f.datasetIngester, f.recordIngester, f.surveyIngester, f.sampleIngester, f.siteIngester, f.recorderIngester, f.featureIngester, f.datasetReset)
    ingester.ingest(f.archive, f.metadata)

    // assert
    verify(f.t, times(2)).commit()
  }
}
