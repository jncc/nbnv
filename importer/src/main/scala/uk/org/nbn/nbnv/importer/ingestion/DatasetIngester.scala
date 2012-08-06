package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.importer.utility._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.KeyGenerator
;

class DatasetIngester(val em: EntityManager, keyGenerator: KeyGenerator) {

  def upsertDataset(metadata: Metadata): TaxonDataset = {

    // we're only dealing with *Taxon*Datasets at the moment
    // try to find the (taxon) dataset in the database
    val taxonDataset = Option(em.find(classOf[TaxonDataset], metadata.datasetKey))

    taxonDataset match {
      case Some(td) => {
        val d = td.getDataset
        modifyDataset(d, metadata)
        td
      }
      case None => {
        // generate a new key and a new dataset
        val key = keyGenerator.nextTaxonDatasetKey
        val d = new Dataset(key)
        modifyDataset(d, metadata)
        em.persist(d)
        // deal with the table-per-class inheritance model (TaxonDataset has-a Dataset)
        val td = new TaxonDataset(key)
        td.setDataset(d)
        em.persist(td)
        td
      }
    }
  }

  private def modifyDataset(dataset: Dataset, metadata: Metadata) = {

    dataset.setAccessConstraints(metadata.accessConstraints)
    dataset.setDataCaptureMethod(metadata.dataCaptureMethod)
//    dataset.setDatasetProvider()
    dataset.setDataQuality(metadata.dataQuality)
    dataset.setDatasetTitle(metadata.datasetTitle)
    dataset.setDatasetTypeKey(em.getReference(classOf[DatasetType], 'T'))
    dataset.setDateUploaded(Clock.nowUtc) // todo: check this
    dataset.setDescription(metadata.description)
    dataset.setGeographicalCoverage(metadata.geographicCoverage)
    dataset.setMetadataLastEdited(Clock.nowUtc) // todo: check this
    dataset.setPurpose(metadata.purpose)
    dataset.setUseConstraints(metadata.useConstraints)
    dataset
  }
}
