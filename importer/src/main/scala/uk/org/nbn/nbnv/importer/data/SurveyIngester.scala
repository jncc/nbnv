/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager;

class SurveyIngester (entityManager : EntityManager ){
  def upsertSurvey(surveyKey : String, dataset : TaxonDataset) : Survey = {

    //todo: Generate surveyKey if blank

    val surveyQuery = entityManager.createQuery("SELECT s FROM Survey WHERE s.surveyKey = :surveyKey AND s.datasetKey = :datasetKey", classOf[Survey])
                      .setParameter("surveyKey", surveyKey)
                      .setParameter("datasetKey", dataset.getDatasetKey)
                      .getResultList
    
    if (!surveyQuery.isEmpty) {
      surveyQuery.get(0)
    }
    else {
      //todo: use the survey key as title when creating a new survey - check this is correct
      val survey = new Survey(surveyKey, surveyKey)
      survey.setDatasetKey(dataset)
      entityManager.persist(survey)
      return survey
    }
  }
}
