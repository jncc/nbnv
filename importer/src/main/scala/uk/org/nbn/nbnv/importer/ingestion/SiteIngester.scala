package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore.{Site, Dataset}
import com.google.inject.Inject
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}

class SiteIngester @Inject()(db: Database) {

  def upsertSite(siteKey: Option[String], siteName: Option[String], dataset: Dataset) {
    
    if (siteKey.isDefined && !db.repo.getSite(siteKey.get, dataset).isDefined) {
    	val s = new Site()
    	val key = siteKey.get
        s.setProviderKey(key)
        s.setName(siteName getOrElse key)
        s.setDataset(dataset)
        db.em.persist(s)
    }
  }
}
