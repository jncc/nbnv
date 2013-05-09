package uk.org.nbn.nbnv.importer.smoke


import java.io.File
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import uk.org.nbn.nbnv.importer._
import ingestion.FeatureIngester
import records.BoundaryDef
import records.BoundaryDef
import spatial.GridSquareInfoFactory
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import data._
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.jpa.nbnimportstaging.StagingPersistenceUtility
import uk.org.nbn.nbnv.importer.BadDataException

class PartialSmokeSuiteIT extends BaseFunSuite with ResourceLoader {

  def fixture = new {
    val qc = new QueryCache(mock[Logger])
    val sqc = new QueryCache(mock[Logger])
    val em = new PersistenceUtility().createEntityManagerFactory(Settings.coreDbSettingsMap).createEntityManager
    val sem = new StagingPersistenceUtility().createEntityManagerFactory(Settings.stagingDbSettingsMap).createEntityManager
    val db = new Database(em, sem, new StagingRepository(mock[Logger], sem, sqc) ,new CoreRepository(mock[Logger], em, qc), qc)
  }

  test("should be able to get next dataset key") {

    def f = fixture
    val kg = new KeyGenerator(f.db)
    val key = kg.nextTaxonDatasetKey

    key should startWith ("GA")
    key should have length 8
  }


  //Data dependant test
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

}
