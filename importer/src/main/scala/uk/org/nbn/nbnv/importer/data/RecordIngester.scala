/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.{EntityManager, Query}
import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.terms.DwcTerm;


class RecordIngester (em : EntityManager) {
  
  def upsertRecord(record : StarRecord, dataset : Dataset) {
    
    /*Query query = entityManager.createQuery("Select sm from SecureMessage sm where sm.someField=:arg1");
     query.setParameter("arg1", arg1);*/

    
    val surveyQuery = em.createQuery("SELECT s FROM Survey WHERE s.surveyKey = :surveyKey AND s.datasetKey = :datasetKey", classOf[Survey])
                      .setParameter("surveyKey", record.core().value(DwcTerm.collectionCode))
                      .setParameter("datasetKey", dataset.getDatasetKey)
    
    val survey : Option[Survey] = Some(surveyQuery.getSingleResult())
    
    val sampleQuery = em.createQuery("SELECT s FROM Sample WHERE s.sampleKey=:sampleKey", classOf[Sample])
                      .setParameter("sampleKey", record.core().value(DwcTerm.eventID))

    val sample : Option[Sample] = Some(sampleQuery.getSingleResult())
    
    
    
  }
}
