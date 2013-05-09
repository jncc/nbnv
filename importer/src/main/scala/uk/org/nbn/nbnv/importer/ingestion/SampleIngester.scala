/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, CoreRepository}
import uk.org.nbn.nbnv.importer.data.Implicits._
import com.google.inject.Inject

class SampleIngester  @Inject()(db: Database) {

  def upsertSample(sampleKey: Option[String], survey: Survey): Sample = {

    val key = sampleKey getOrElse "1"
    val sample = db.coreRepo.getSample(key, survey)

    sample match {
      case Some(s) => s
      case None => {
        val s = new Sample()
        s.setProviderKey(key)
        s.setSurvey(survey)
        db.em.persist(s)
        s
      }
    }
  }
}
