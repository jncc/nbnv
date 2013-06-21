package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore.{ImportSite, ImportDataset, Site, Dataset}
import com.google.inject.Inject
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import org.apache.log4j.Logger

class SiteIngester @Inject()(db: Database, log: Logger) {

  def stageSite(siteKey: Option[String], siteName: Option[String], dataset: ImportDataset) {
    
    if (siteKey.isDefined && !db.repo.getImportSite(siteKey.get, dataset).isDefined) {
    	val s = new ImportSite()
    	val key = siteKey.get

      log.info("Upserting site %s for dataset %s".format(key, dataset.getProviderKey))

      s.setProviderKey(key)
      s.setName(siteName getOrElse key)
      s.setDatasetKey(dataset)
      db.em.persist(s)
    }
  }
}
