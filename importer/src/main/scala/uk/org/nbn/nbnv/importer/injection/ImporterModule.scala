package uk.org.nbn.nbnv.importer.injection

import com.google.inject._
import uk.org.nbn.nbnv.importer.ingestion.DatasetIngester
import uk.org.nbn.nbnv.PersistenceUtility
import uk.org.nbn.nbnv.importer.{Options, Settings}
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.logging.Log
import org.apache.log4j.Level


class ImporterModule(options: Options) extends AbstractModule {
  var entityManager: Option[EntityManager] = None

  def configure() {
    Log.configure(options.logDir, "4MB", Level.ALL)
  }

  @Provides
  def getOptions = options

  @Provides
  def getLog = Log.get()

  @Provides
  def getEntityManager = {
    entityManager match {
      case Some(em) => em
      case None => {
        val em = new PersistenceUtility().createEntityManagerFactory(Settings.map).createEntityManager
        entityManager = Some(em)
        em
      }
    }
  }
}
