package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore.Recorder
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.Repository
import com.google.inject.Inject

class RecorderIngester @Inject()(em: EntityManager, repo: Repository) {

  def ensureRecorder(name: String) = {

    repo.getFirstRecorder(name) match {
      case Some(r) => r
      case None => {
        // todo: create / insert recorder
        val r = new Recorder
        r
      }
    }
  }
}
