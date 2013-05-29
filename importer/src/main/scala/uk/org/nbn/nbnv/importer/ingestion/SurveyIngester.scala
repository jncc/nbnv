
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import com.google.inject.Inject
;

class SurveyIngester @Inject()(db: Database) {

  def stageSurvey(surveyKey: Option[String], dataset: ImportTaxonDataset) {

    val key = surveyKey getOrElse "1" // if no survey key provided

    if (!db.repo.getImportSurvey(key, dataset).isDefined) {
      val s = new ImportSurvey()
      s.setProviderKey(key)
      s.setTitle(key)
      s.setDatasetKey(dataset)
      db.em.persist(s)
    }
  }
}
