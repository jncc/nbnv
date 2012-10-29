package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore.{Site, Dataset}
import com.google.inject.Inject
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}

class SiteIngester @Inject()(db: Database) {

  def upsertSite(siteKey: Option[String], siteName: Option[String], dataset: Dataset): Option[Site] = {

    siteKey match {
      case None => None
      case Some(key) => {
        db.repo.getSite(key, dataset) match {
          case Some(s) => Some(s)
          case None => {
            val s = new Site()
            s.setProviderKey(key)
            s.setName(siteName getOrElse key)
            s.setDataset(dataset)
            db.em.persist(s)
            Some(s)
          }
        }
      }
    }

  }
}
