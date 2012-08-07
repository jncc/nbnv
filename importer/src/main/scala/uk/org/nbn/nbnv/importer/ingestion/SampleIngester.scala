/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager;

class SampleIngester(entityManager: EntityManager) {
  def upsertSample(sampleKey: String, survey: Survey): Sample = {

    //todo: Generate sampleKey if blank
    val sampleQuery = entityManager.createQuery("SELECT s FROM Sample s WHERE s.sampleKey=:sampleKey AND s.surveyID = :surveyID", classOf[Sample])
      .setParameter("sampleKey", sampleKey)
      .setParameter("surveyID", survey)
      .getResultList

    if (!sampleQuery.isEmpty) {
      sampleQuery.get(0)
    }
    else {
      val sample = new Sample()
      sample.setSampleKey(sampleKey)
      sample.setSurveyID(survey)
      entityManager.persist(sample)
      return sample
    }
  }
}
