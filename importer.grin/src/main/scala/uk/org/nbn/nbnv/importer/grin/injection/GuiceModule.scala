package uk.org.nbn.nbnv.importer.grin.injection

import uk.org.nbn.nbnv.importer.Settings
import com.google.inject.{Provides, Singleton, AbstractModule}
import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.importer.logging.Log
import org.apache.log4j.Level
import uk.org.nbn.nbnv.importer.data.QueryCache
import uk.org.nbn.nbnv.importer.grin.Options

class GuiceModule(options: Options) extends AbstractModule {

  val entityManager = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager
  Log.configure(options.logDir, "4MB", Level.ALL)

  override def configure() {
    // kinda obvious why the cache should be a singleton...
    bind(classOf[QueryCache]).in(classOf[Singleton])
  }

  @Provides
  def getOptions = options

  @Provides
  def getLog = Log.get()

  @Provides
  def getEntityManager = entityManager
}