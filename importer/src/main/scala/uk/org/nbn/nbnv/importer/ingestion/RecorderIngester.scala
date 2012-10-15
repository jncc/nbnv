package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore.Recorder
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import com.google.inject.Inject

class RecorderIngester @Inject()(db: Database) {

  def ensureRecorder(name: String) = {

    db.repo.getFirstRecorder(name) match {
      case Some(r) => r
      case None => {
        val r = new Recorder()
        r.setName(name)
        db.em.persist(r)
        r
      }
    }
  }
}
