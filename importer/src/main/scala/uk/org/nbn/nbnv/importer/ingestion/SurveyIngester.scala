
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.Repository
;

class SurveyIngester(entityManager: EntityManager, repository: Repository) {

  def upsertSurvey(surveyKey: String, dataset: TaxonDataset): Survey = {

    val key = if (surveyKey == "") "1" else surveyKey

    def update(s: Survey) {
      s.setSurveyKey(key)
      s.setTitle(key)
      s.setDatasetKey(dataset)
    }

    val survey = repository.getSurvey(key, dataset)

    survey match {
      case Some(s) => {
        // todo: do we need to update?
        update(s)
        s
      }
      case None => {
        val s = new Survey
        update(s)
        entityManager.persist(s)
        s
      }
    }
  }
}
