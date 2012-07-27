/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager;
import org.gbif.dwc.text.StarRecord

class RecordIngester (em : EntityManager) {
  def upsertRecord(record : StarRecord) {
    
  }
}
