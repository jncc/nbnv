
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, CoreRepository}
import com.google.inject.Inject
;

class SurveyIngester @Inject()(db: Database) {

  def upsertSurvey(surveyKey: Option[String], dataset: TaxonDataset): Survey = {

    val key = surveyKey getOrElse "1" // if no survey key provided

    def update(s: Survey) {
      s.setProviderKey(key)
      s.setTitle(key)
      s.setTaxonDataset(dataset)
    }

    val survey = db.coreRepo.getSurvey(key, dataset) getOrElse {
      val s = new Survey
      update(s)
      db.em.persist(s)
      s
    }

    db.em.flush() // to get survey key for proceeding sample caching
    survey
  }
}
