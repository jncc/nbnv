/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.importer.utility._
import javax.persistence.EntityManager;

class DatasetIngester (val em : EntityManager) {
  def upsertDataset(metadata : Metadata) : Dataset = {
      
    def mergeDataset( dataset : Dataset, metadata : Metadata) : Dataset = {
      
      dataset.setAccessConstraints(metadata.accessConstraints)
      dataset.setDataCaptureMethod(metadata.dataCaptureMethod)
      dataset.setDataQuality(metadata.dataQuality)
      dataset.setDatasetTitle(metadata.datasetTitle)
      dataset.setDatasetTypeKey(em.getReference(classOf[DatasetType],'T'))
      dataset.setDateUploaded(Clock.nowUtc) //todo: check this
      dataset.setDescription(metadata.description)
      dataset.setGeographicalCoverage(metadata.geographicCoverage)
      dataset.setMetadataLastEdited(Clock.nowUtc) //todo: check this
      dataset.setPurpose(metadata.purpose)
      dataset.setUseConstraints(metadata.useConstraints)
      
      //Returns the merged instance of the dataset entity
      em.merge(dataset)
    }
    
    val dataset : Option[Dataset] = Some(em.find(classOf[Dataset], metadata.datasetKey))
    
    dataset match {
      case Some(ds) => mergeDataset(ds, metadata)
      case None => mergeDataset(new Dataset, metadata)
    }
  }
}
