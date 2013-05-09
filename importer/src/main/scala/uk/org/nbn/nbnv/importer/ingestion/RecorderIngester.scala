package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore.Recorder
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, CoreRepository}
import com.google.inject.Inject

class RecorderIngester @Inject()(db: Database) {

  def ensureRecorder(name: Option[String]) = {

    name match {
      case None => None
      case Some(n) => {
        val recorder = db.coreRepo.getFirstRecorder(n) getOrElse {
          val r = new Recorder()
          r.setName(n)
          db.em.persist(r)
          r
        }
        Some(recorder)
      }
    }
  }
}
