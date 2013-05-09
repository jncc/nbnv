package uk.org.nbn.nbnv.importer.injection

import com.google.inject._
import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.jpa.nbnimportstaging.StagingPersistenceUtility
import uk.org.nbn.nbnv.importer.{Options, Settings}
import uk.org.nbn.nbnv.importer.logging.Log
import org.apache.log4j.Level
import uk.org.nbn.nbnv.importer.data.{CoreRepository, Database, QueryCache}


class ImporterModule(options: Options) extends AbstractModule {

  Log.configure(options.logDir, options.archivePath, options.logLevel)
  val log = Log.get()

  val sem = new StagingPersistenceUtility().createEntityManagerFactory(Settings.stagingDbSettingsMap).createEntityManager
  val em = new PersistenceUtility().createEntityManagerFactory(Settings.coreDbSettingsMap).createEntityManager
  val qc = new QueryCache(log)
  val db = new Database(em, sem, new CoreRepository(log, em, qc), qc)


  def configure() {} 

  @Provides
  def getOptions = options

  @Provides
  def getLog = log

  @Provides
  def getDatabase = db

}
