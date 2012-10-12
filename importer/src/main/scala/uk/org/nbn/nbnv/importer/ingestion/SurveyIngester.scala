
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import com.google.inject.Inject
;

class SurveyIngester @Inject()(db: Database) {

  def upsertSurvey(surveyKey: String, dataset: TaxonDataset): Survey = {

    val key = if (surveyKey == "") "1" else surveyKey

    def update(s: Survey) {
      s.setProviderKey(key)
      s.setTitle(key)
      s.setTaxonDataset(dataset)
    }

    val survey = db.repo.getSurvey(key, dataset)

    survey match {
      case Some(s) => s
      case None => {
        val s = new Survey
        update(s)
        db.em.persist(s)
        s
      }
    }
  }
}
