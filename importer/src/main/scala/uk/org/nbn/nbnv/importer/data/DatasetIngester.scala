/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager;

class DatasetIngester (val em : EntityManager) {
  def upsertDataset(metadata : Metadata) : Dataset = {
    val dataSet = em.find(classOf[Dataset], metadata.datasetKey)
    
    return dataSet
  }
}
