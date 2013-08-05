package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore.{ImportSite, ImportDataset, Site, Dataset}
import com.google.inject.Inject
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import org.apache.log4j.Logger

class SiteIngester @Inject()(db: Database, log: Logger) {

  def upsertSite(siteKey: Option[String], siteName: Option[String], dataset: ImportDataset): Option[ImportSite] = {

    if (siteKey.isDefined) {
      val key = siteKey.get

      db.repo.getImportSiteByKey(key, dataset) match {
        case Some(s) => Some(s)
        case None => {
          val s = new ImportSite()
          s.setProviderKey(key)
          s.setName(siteName getOrElse key)
          s.setDatasetKey(dataset)
          db.em.persist(s)
          Some(s)
        }
      }
    }
    else if (siteName.isDefined) {
      val name = siteName.get

      db.repo.getImportSiteByName(name, dataset) match {
        case Some(s) => Some(s)
        case None => {
          val s = new ImportSite()
          s.setName(siteName.get)
          s.setDatasetKey(dataset)
          db.em.persist(s)
          Some(s)
        }
      }
    }
    else {
      None
    }
  }
}
