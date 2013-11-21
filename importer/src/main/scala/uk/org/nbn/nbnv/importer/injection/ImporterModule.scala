package uk.org.nbn.nbnv.importer.injection

import com.google.inject._
import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.importer.{Options, Settings}
import uk.org.nbn.nbnv.importer.logging.Log
import org.apache.log4j.Level
import uk.org.nbn.nbnv.importer.data.{Repository, Database, QueryCache}
import uk.org.nbn.nbnv.importer.jersey.WebResourceFactory


class ImporterModule(options: Options) extends AbstractModule {

  Log.configure(options.logDir, options.archivePath, options.logLevel)
  val log = Log.get()

  val em = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager
  val qc = new QueryCache(log)
  val db = new Database(em, new Repository(log, em, qc), qc)

  def configure() {} // nothing to do here

  @Provides
  def getOptions = options

  @Provides
  def getLog = log

  @Provides
  def getDatabase = db

  @Provides
  def getWebResource = WebResourceFactory.getWebResource()
}
