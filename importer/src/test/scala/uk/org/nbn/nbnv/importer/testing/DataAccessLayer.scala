package uk.org.nbn.nbnv.importer.testing

import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.importer.Settings
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.data.{Repository, Database, QueryCache}
import org.apache.log4j.Logger

class DataAccessLayer {
  def getDatabase = {
    val em = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager
    val log = mock(classOf[Logger])
    val cache = new QueryCache(log)

    new Database(em, new Repository(log, em, cache), cache)
  }
}
