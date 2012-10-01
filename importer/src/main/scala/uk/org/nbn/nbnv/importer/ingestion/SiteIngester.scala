package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore.{Site, Dataset}
import com.google.inject.Inject
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.Repository

class SiteIngester @Inject()(em: EntityManager, repository: Repository){
  def upsertSite(siteKey: String, siteName: String, dataset: Dataset): Option[Site] = {

    if (siteKey == "")
      None
    else {

      repository.getSite(siteKey, dataset) match {
        case Some(s) => Some(s)
        case None => {
          val s = new Site()
          s.setProviderKey(siteKey)
          s.setName(siteName)
          s.setDatasetKey(dataset)
          em.persist(s)
          Some(s)
        }
      }
    }
  }
}
