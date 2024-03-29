package uk.org.nbn.nbnv.importer.smoke


import uk.org.nbn.nbnv.importer._
import ingestion.FeatureIngester
import reset.DatasetReset
import records.BoundaryDef
import spatial.GridSquareInfoFactory
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import data.{QueryCache, Database, Repository, KeyGenerator}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.importer.Settings


class PartialSmokeSuiteIT extends BaseFunSuite with ResourceLoader {

  def fixture = new {
    val qc = new QueryCache(mock[Logger])
    val em = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager
    val db = new Database(em, new Repository(mock[Logger], em, qc), qc)
  }

  test("should be able to get next dataset key") {

    def f = fixture
    val kg = new KeyGenerator(f.db)
    val key = kg.nextTaxonDatasetKey

    key should startWith ("GA")
    key should have length 8
  }

  //todo: we need a test database for this to work
  ignore("should be able to get a site boundary feature") {

    val f = fixture
    val i = new FeatureIngester(mock[Logger], f.db, new GridSquareInfoFactory(f.db))

    i.ensureSiteBoundaryFeature(BoundaryDef("GA000942E012"))
  }

  test("should throw on non-existent site boundary feature") {

    val f = fixture
    val i = new FeatureIngester(mock[Logger], f.db, new GridSquareInfoFactory(f.db))

    val ex = intercept[BadDataException] {
      i.ensureSiteBoundaryFeature(BoundaryDef("THISDOESNOTEXIST"))
    }

    ex.message should include ("Expected one result for 'THISDOES|NOTEXIST', but found none")
  }

  ignore("ingester sproc should throw exception when failing"){
    def f = fixture

    f.db.repo.importTaxonObservationsAndRelatedRecords()
  }

  ignore("should call external dataset access reset tool") {
    val datasetReset = new DatasetReset
    datasetReset.resetDatasetAccess("GA001280")
  }

  ignore("check call to set dateset public"){
    val f = fixture
    f.db.repo.setDatasetPublic("GA001268")
  }




}
