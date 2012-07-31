/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager;

class SurveyIngester (em : EntityManager ){
  def upsertSurvey(surveyKey : String, dataset : TaxonDataset) : Survey = {

    val surveyQuery = em.createQuery("SELECT s FROM Survey WHERE s.surveyKey = :surveyKey AND s.datasetKey = :datasetKey", classOf[Survey])
                      .setParameter("surveyKey", surveyKey)
                      .setParameter("datasetKey", dataset.getDatasetKey)
                      .getResultList
    
    //todo: Generate a new sample key if none given.
    //todo: use the survey key as title when creating a new survey - check this is correct
    if (!surveyQuery.isEmpty) {
      surveyQuery.get(0)
    }
    else {
      val survey = new Survey(surveyKey, surveyKey)
      survey.setDatasetKey(dataset)
      return survey
    }
  }
}
