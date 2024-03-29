package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore.ImportRecorder
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import com.google.inject.Inject
import org.apache.log4j.Logger

class RecorderIngester @Inject()(db: Database, log:Logger) {

  def ensureRecorder(name: Option[String]) {
    
    if (name.isDefined && !db.repo.getFirstImportRecorder(name.get).isDefined) {

      log.info("Upserting recorder %s ".format(name.get))

    	val r = new ImportRecorder()
    	r.setName(name.get)
    	db.em.persist(r)
    }
  }
}
