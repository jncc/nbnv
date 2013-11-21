package uk.org.nbn.nbnv.importer.smoke


import java.io.File
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import uk.org.nbn.nbnv.importer._
import ingestion.FeatureIngester
import jersey.WebResourceFactory
import records.BoundaryDef
import spatial.GridSquareInfoFactory
import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.ResourceLoader
import data.{QueryCache, Database, Repository, KeyGenerator}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.PersistenceUtility
import javax.ws.rs.core.{NewCookie, MediaType}
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.core.util.MultivaluedMapImpl
import scala.collection.JavaConversions._

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

//  Requires the web services to be availbe at the url set in api.url (importer.properties test resource)
  ignore("can call web resource")  {
    val resource = WebResourceFactory.getWebResource()

    val formData = new MultivaluedMapImpl()

    formData.add("username", "matt.debont")
    formData.add("password", "v8PK4Iz9J$g0G@U")

    val acResponse = resource.path("/user/login").`type`(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(classOf[ClientResponse], formData);

    val cookies  = acResponse.getCookies

    val authCookie  = cookies.find(c => c.getName == "nbn.token_key").head

    val oaPath = "/user/userAccesses/reset/GA001280"

//    val oaPath = "/organisation/organisationAccesses/reset/GA001280"

    val oaResponse = resource.path(oaPath).cookie(authCookie).accept(MediaType.APPLICATION_JSON).get(classOf[ClientResponse])

    oaResponse.getStatus() should be (200)

    val logOutResponse = resource.path("/user/logout").cookie(authCookie).accept(MediaType.APPLICATION_JSON).get(classOf[ClientResponse])

    logOutResponse.getStatus() should be (200)

  }

}
