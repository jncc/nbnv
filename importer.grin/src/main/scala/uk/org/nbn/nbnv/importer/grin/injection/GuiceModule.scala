package uk.org.nbn.nbnv.importer.grin.injection

import uk.org.nbn.nbnv.importer.Settings
import com.google.inject.{Provides, Singleton, AbstractModule}
import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.importer.logging.Log
import org.apache.log4j.Level
import uk.org.nbn.nbnv.importer.data.{StagingRepository, CoreRepository, Database, QueryCache}
import uk.org.nbn.nbnv.importer.grin.Options
import uk.org.nbn.nbnv.jpa.nbnimportstaging.StagingPersistenceUtility

class GuiceModule(options: Options) extends AbstractModule {

  Log.configure(options.logDir, "4MB", "INFO")
  val log = Log.get()

  val em = new PersistenceUtility().createEntityManagerFactory(Settings.coreDbSettingsMap).createEntityManager
  val sem = new StagingPersistenceUtility().createEntityManagerFactory(Settings.coreDbSettingsMap).createEntityManager
  val qc = new QueryCache(log)
  val sqc = new QueryCache(log)
  val db = new Database(em, sem, new StagingRepository(log, sem, sqc) ,new CoreRepository(log, em, qc), qc)

  def configure() {} // nothing to do here

  @Provides
  def getOptions = options

  @Provides
  def getLog = log

  @Provides
  def getDatabase = db
}
