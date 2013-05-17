
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import com.google.inject.Inject
;

class SurveyIngester @Inject()(db: Database) {

  def upsertSurvey(surveyKey: Option[String], dataset: TaxonDataset) {

    val key = surveyKey getOrElse "1" // if no survey key provided

    if (!db.repo.getSurvey(key, dataset).isDefined) {
      val s = new Survey
      s.setProviderKey(key)
      s.setTitle(key)
      s.setTaxonDataset(dataset)
      db.em.persist(s)
    }
  }
}
