
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import com.google.inject.Inject
import org.apache.log4j.Logger
;

class SurveyIngester @Inject()(db: Database, log: Logger) {

  def stageSurvey(surveyKey: Option[String], dataset: ImportTaxonDataset) {

    val key = surveyKey getOrElse "1" // if no survey key provided

    if (!db.repo.getImportSurvey(key, dataset).isDefined) {
      log.info("Upserting survey %s for dataset %s".format(key, dataset.getDatasetKey))

      val s = new ImportSurvey()
      s.setProviderKey(key)
      s.setDatasetKey(dataset)
      db.em.persist(s)
    }
  }
}
