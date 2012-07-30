/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.terms.DwcTerm;


class RecordIngester (em : EntityManager) {
  
  def upsertRecord(record : StarRecord, dataset : Dataset) {
    val survey : Option[Survey] = Some(em.find(classOf[Survey], record.core().value(DwcTerm.collectionCode)))
    
    val sample : Option[Sample] = Some(em.find(classOf[Sample], record.core().value(DwcTerm.eventID)))
    
    
    
  }
}
