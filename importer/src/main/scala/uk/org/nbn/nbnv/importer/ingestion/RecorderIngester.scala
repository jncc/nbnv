package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore.Recorder
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import com.google.inject.Inject

class RecorderIngester @Inject()(db: Database) {

  def ensureRecorder(name: Option[String]) {
    
    if (name.isDefined && !db.repo.getFirstRecorder(name.get).isDefined) {
    	val r = new Recorder()
    	r.setName(name.get)
    	db.em.persist(r)
    }
  }
}
