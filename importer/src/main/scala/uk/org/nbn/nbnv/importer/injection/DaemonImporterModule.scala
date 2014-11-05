package uk.org.nbn.nbnv.importer.injection

import com.google.inject._
import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.importer.{Options, Settings}
import org.apache.log4j._
import uk.org.nbn.nbnv.importer.data.{Repository, Database, QueryCache}


class DaemonImporterModule(options: Options, logger: Logger) extends AbstractModule {

  val log = logger

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
}
