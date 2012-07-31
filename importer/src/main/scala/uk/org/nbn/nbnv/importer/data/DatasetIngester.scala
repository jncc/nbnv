package uk.org.nbn.nbnv.importer.data

import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.importer.utility._
import javax.persistence.EntityManager;

class DatasetIngester (val em: EntityManager) {

  def upsertDataset(metadata: Metadata) : TaxonDataset = {

        // returns the merged instance of the dataset entity
    def mergeDataset(dataset: Dataset, metadata: Metadata) : Dataset = {

      dataset.setAccessConstraints(metadata.accessConstraints)
      dataset.setDataCaptureMethod(metadata.dataCaptureMethod)
      dataset.setDataQuality(metadata.dataQuality)
      dataset.setDatasetTitle(metadata.datasetTitle)
      dataset.setDatasetTypeKey(em.getReference(classOf[DatasetType],'T'))
      dataset.setDateUploaded(Clock.nowUtc) // todo: check this
      dataset.setDescription(metadata.description)
      dataset.setGeographicalCoverage(metadata.geographicCoverage)
      dataset.setMetadataLastEdited(Clock.nowUtc) // todo: check this
      dataset.setPurpose(metadata.purpose)
      dataset.setUseConstraints(metadata.useConstraints)

      em.merge(dataset)
    }
    
    Option(em.find(classOf[TaxonDataset], metadata.datasetKey)) match {
      case Some(taxonDataset) => {
          taxonDataset.setDataset(mergeDataset(taxonDataset.getDataset, metadata))
          return taxonDataset
      }
      case None => {
          // todo: need a mechanism to generate a new dataset key
          val dataset = mergeDataset(new Dataset, metadata)
          val taxonDataset = new TaxonDataset(dataset.getDatasetKey)
          taxonDataset.setDataset(dataset)
          
          em.persist(taxonDataset)
          
          return taxonDataset
      }
    }    
  }
}
