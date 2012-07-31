/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.data
import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager;

class SampleIngester (entityManager : EntityManager) {
  def upsertSample(sampleKey : String, survey : Survey) : Sample = {

    val sampleQuery = entityManager.createQuery("SELECT s FROM Sample WHERE s.sampleKey=:sampleKey AND s.surveyKey = :surveyKey", classOf[Sample])
                      .setParameter("sampleKey", sampleKey)
                      .setParameter("surveyKey", survey.getSurveyKey)
                      .getResultList

    if (!sampleQuery.isEmpty) {
      sampleQuery.get(0) 
    }
    else {
      val sample = new Sample(sampleKey)
      sample.setSurveyID(survey)
      entityManager.persist(sample)
      return sample
    } 
  }
}
