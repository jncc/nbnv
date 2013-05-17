package uk.org.nbn.nbnv.importer.ingestion

import org.mockito.Mockito._
import javax.persistence.{EntityTransaction, EntityManager}
import uk.org.nbn.nbnv.importer.metadata.Metadata
import org.gbif.dwc.text.{StarRecord, Archive}
import org.gbif.utils.file.ClosableIterator
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.{Target, Options}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.data.{QueryCache, Repository, Database}

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

    val archive = mock[Archive]
    val iterator = mock[ClosableIterator[StarRecord]]
    when(archive.iteratorRaw).thenReturn(iterator)

    val metadata = mock[Metadata]
    val db = new Database(em, mock[Repository], mock[QueryCache])

  }

  test("should begin transaction") {

    val f = fixture

    // act
    val ingester = new Ingester(f.options, mock[Logger], f.db, f.datasetIngester, f.recordIngester, f.surveyIngester, f.sampleIngester, f.siteIngester, f.recorderIngester, f.featureIngester)
    ingester.ingest(f.archive, f.metadata)

    // assert
    verify(f.t).begin()
  }

  test("should commit transaction") {

    val f = fixture

    // act
    val ingester = new Ingester(f.options, mock[Logger], f.db, f.datasetIngester, f.recordIngester, f.surveyIngester, f.sampleIngester, f.siteIngester, f.recorderIngester, f.featureIngester)
    ingester.ingest(f.archive, f.metadata)

    // assert
    verify(f.t).commit()
  }
}
