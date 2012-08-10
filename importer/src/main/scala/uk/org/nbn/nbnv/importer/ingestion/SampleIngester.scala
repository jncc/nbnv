/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.data.Implicits._

class SampleIngester(em: EntityManager) {

  def upsertSample(sampleKey: String, survey: Survey): Sample = {

    val key = if (sampleKey == "") "1" else sampleKey

    //todo: move to repository
    def getSample(key: String, survey: Survey) = {
      em.createQuery("SELECT s FROM Sample s WHERE s.sampleKey=:sampleKey AND s.surveyID = :surveyID", classOf[Sample])
        .setParameter("sampleKey", sampleKey)
        .setParameter("surveyID", survey)
        .getSingleOrNone
    }

    val sample = getSample(key, survey)

    sample match {
      case Some(s) => s
      case None => {
        val s = new Sample()
        s.setSampleKey(key)
        s.setSurveyID(survey)
        em.persist(s)
        s
      }
    }
  }
}
