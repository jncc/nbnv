package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore.Recorder
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.Repository

class RecorderIngester(em: EntityManager, r: Repository) {

  def ensureRecorder(name: String) = {

    r.getFirstRecorder(name) match {
      case Some(recorder) => recorder
      case _ => {
        val recorder = new Recorder
        recorder
        // todo: insert recorder
        // todo: will inserting a recorder mean it's available in subsequent queries? probably not!
      }
    }
  }
}
