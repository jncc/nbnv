
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.Repository
;

class SurveyIngester(entityManager: EntityManager, repository: Repository) {

  def upsertSurvey(surveyKey: String, dataset: TaxonDataset): Survey = {

    def update(s: Survey) {
      s.setSurveyKey(surveyKey)
      s.setTitle(surveyKey)
      s.setDatasetKey(dataset)
    }

    val survey = repository.getSurvey(surveyKey, dataset)

    survey match {
      case Some(s) => {
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
