package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore.{Site, Dataset}
import com.google.inject.Inject
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}

class SiteIngester @Inject()(db: Database){
  def upsertSite(siteKey: String, siteName: String, dataset: Dataset): Option[Site] = {

    if (siteKey == "")
      None
    else {

      db.repo.getSite(siteKey, dataset) match {
        case Some(s) => Some(s)
        case None => {
          val s = new Site()
          s.setProviderKey(siteKey)
          s.setName(siteName)
          s.setDataset(dataset)
          db.em.persist(s)
          Some(s)
        }
      }
    }
  }
}
