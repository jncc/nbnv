package uk.org.nbn.nbnv.importer.injection

import com.google.inject._
import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.importer.{Options, Settings}
import uk.org.nbn.nbnv.importer.logging.Log
import org.apache.log4j.Level


class ImporterModule(options: Options) extends AbstractModule {

  val entityManager = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager
  Log.configure(options.logDir, "4MB", Level.ALL)

  override def configure() {}

  @Provides
  def getOptions = options

  @Provides
  def getLog = Log.get()

  @Provides
  def getEntityManager = entityManager
}
