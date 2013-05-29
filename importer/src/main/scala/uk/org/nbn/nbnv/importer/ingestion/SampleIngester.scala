/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import uk.org.nbn.nbnv.importer.data.Implicits._
import com.google.inject.Inject

class SampleIngester  @Inject()(db: Database) {

  def stageSample(sampleKey: Option[String], survey: ImportSurvey) {

    val key = sampleKey getOrElse "1"

    if (!db.repo.getImportSample(key, survey).isDefined) {
        val s = new ImportSample()
        s.setProviderKey(key)
        s.setSurveyID(survey)
        db.em.persist(s)
    }
  }
}
