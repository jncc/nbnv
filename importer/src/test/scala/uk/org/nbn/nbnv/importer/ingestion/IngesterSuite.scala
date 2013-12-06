package uk.org.nbn.nbnv.importer.ingestion

import org.mockito.Mockito._
import javax.persistence.{EntityTransaction, EntityManager}
import uk.org.nbn.nbnv.importer.metadata.{Mode, Metadata}
import org.gbif.dwc.text.{StarRecord, Archive}
import org.gbif.utils.file.ClosableIterator
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.{Target, Options}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.data.{QueryCache, Repository, Database}
import com.sun.jersey.api.client.WebResource
import uk.org.nbn.nbnv.importer.jersey.WebApi

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
    val webApi = mock[WebApi]

    val archive = mock[Archive]
    val iterator = mock[ClosableIterator[StarRecord]]
    when(archive.iteratorRaw).thenReturn(iterator)

    val metadata = mock[Metadata]
    when(metadata.datasetKey).thenReturn("")
    when(metadata.importType).thenReturn(Some(Mode.upsert))

    val db = new Database(em, mock[Repository], mock[QueryCache])

  }

  test("should begin transaction") {

    val f = fixture

    // act
    val ingester = new Ingester(f.options, mock[Logger], f.db, f.datasetIngester, f.recordIngester, f.surveyIngester, f.sampleIngester, f.siteIngester, f.recorderIngester, f.featureIngester, f.webApi)
    ingester.ingest(f.archive, f.metadata)

    // assert

    verify(f.t, times(2)).begin()
  }

  test("should commit transaction") {

    val f = fixture

    // act
    val ingester = new Ingester(f.options, mock[Logger], f.db, f.datasetIngester, f.recordIngester, f.surveyIngester, f.sampleIngester, f.siteIngester, f.recorderIngester, f.featureIngester, f.webApi)
    ingester.ingest(f.archive, f.metadata)

    // assert
    verify(f.t, times(2)).commit()
  }
}
