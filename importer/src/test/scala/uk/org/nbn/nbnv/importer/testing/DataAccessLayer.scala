package uk.org.nbn.nbnv.importer.testing

import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.importer.Settings
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.data.{CoreRepository, Database, QueryCache}
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.jpa.nbnimportstaging.StagingPersistenceUtility

class DataAccessLayer {
  def getDatabase = {
    val em = new PersistenceUtility().createEntityManagerFactory(Settings.coreDbSettingsMap).createEntityManager
    val sem = new StagingPersistenceUtility().createEntityManagerFactory(Settings.stagingDbSettingsMap).createEntityManager
    val log = mock(classOf[Logger])
    val cache = new QueryCache(log)

    new Database(em, sem, new CoreRepository(log, em, cache), cache)
  }
}
