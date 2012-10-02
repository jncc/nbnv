
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.Repository
import com.google.inject.Inject
;

class SurveyIngester @Inject()(entityManager: EntityManager, repository: Repository) {

  def upsertSurvey(surveyKey: String, dataset: TaxonDataset): Survey = {

    val key = if (surveyKey == "") "1" else surveyKey

    def update(s: Survey) {
      s.setProviderKey(key)
      s.setTitle(key)
      s.setDatasetKey(dataset)
    }

    val survey = repository.getSurvey(key, dataset)

    survey match {
      case Some(s) => s
      case None => {
        val s = new Survey
        update(s)
        entityManager.persist(s)
        s
      }
    }
  }
}
