package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.importer.utility._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Repository, KeyGenerator}
import org.apache.log4j.Logger
import com.google.inject.Inject

class DatasetIngester @Inject()(log: Logger,
                                em: EntityManager,
                                keyGenerator: KeyGenerator,
                                repository: Repository) {

  def upsertDataset(metadata: Metadata): TaxonDataset = {

    // we're only dealing with *Taxon* Datasets at the moment

    // when there's no key given, insert a new dataset
    // when there is one, find it and update it (otherwise throw)

    if (metadata.datasetKey.isEmpty) {
      insertNew(metadata)
    }
    else {
      // todo: delete existing data?
      updateExisting(metadata)
    }
  }

  def insertNew(metadata: Metadata) = {

    // generate a new key and a new dataset
    val key = keyGenerator.nextTaxonDatasetKey

    log.info("Inserting new dataset " + key)

    val d = new Dataset(key)
    modifyDataset(d, metadata)
    em.persist(d)

    // deal with the table-per-class inheritance model (TaxonDataset has-a Dataset)
    val td = new TaxonDataset(key)
    td.setDataset(d)
    modifyTaxonDataset(td, metadata)
    em.persist(td)

    td
  }

  def updateExisting(metadata: Metadata) = {

    log.info("Updating existing dataset " + metadata.datasetKey)

    val td = repository.getTaxonDataset(metadata.datasetKey)
    modifyTaxonDataset(td, metadata)
    val d = td.getDataset
    modifyDataset(d, metadata)
    td
  }

  def modifyDataset(d: Dataset, m: Metadata) = {

    val provider = repository.getOrganisation(m.datasetProviderName)
    val datasetUpdateFrequency = em.getReference(classOf[DatasetUpdateFrequency], "012")
    val datasetType = em.getReference(classOf[DatasetType], 'T')

    // certain fields are metadata, and we have to record when any changes
    var metadataChanged = false

    def setMetadata[T](value: T, get: () => Any, set: T => Unit) {
      metadataChanged = get != value
      set(value)
    }

    setMetadata(m.dataCaptureMethod, d.getDataCaptureMethod, d.setDataCaptureMethod)
    setMetadata(m.accessConstraints, d.getAccessConstraints, d.setAccessConstraints)
    setMetadata(m.dataQuality, d.getDataQuality, d.setDataQuality)
    setMetadata(m.datasetTitle, d.getDatasetTitle, d.setDatasetTitle)
    setMetadata(m.description, d.getDescription, d.setDescription)
    setMetadata(m.geographicCoverage, d.getGeographicalCoverage, d.setGeographicalCoverage)
    setMetadata(m.purpose, d.getPurpose, d.setPurpose)
    setMetadata(m.useConstraints, d.getUseConstraints, d.setUseConstraints)
    setMetadata(m.additionalInformation, d.getAdditionalInformation, d.setAdditionalInformation)
    setMetadata(m.temporalCoverage, d.getTemporalCoverage, d.setTemporalCoverage)

    d.setDatasetProvider(provider) // not metadata
    d.setDatasetTypeKey(datasetType) // never changes, always 'T'
    d.setDateUploaded(Clock.nowUtc) // date of this import
    d.setUpdateFrequency(datasetUpdateFrequency) // never changes, always '012'

    if (metadataChanged)
      d.setMetadataLastEdited(Clock.nowUtc)

    d
  }




  private def modifyTaxonDataset(td: TaxonDataset, m: Metadata) {

    val resolution = em.getReference(classOf[Resolution], 1.toShort)
    td.setPublicResolution(resolution) // column will be deleted
    td.setMaxResolution(resolution)    // column will be deleted

    // default .. to be read from extra metadata.
    // ...could be more columns like this
    td.setAllowRecordValidation(true)
  }
}
