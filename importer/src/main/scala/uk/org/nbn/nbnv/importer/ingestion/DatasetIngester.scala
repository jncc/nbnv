package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.importer.utility._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository, KeyGenerator}
import org.apache.log4j.Logger
import com.google.inject.Inject

class DatasetIngester @Inject()(log: Logger,
                                db: Database,
                                keyGenerator: KeyGenerator) {

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
    db.em.persist(d)

    // deal with the table-per-class inheritance model (TaxonDataset has-a Dataset)
    val td = new TaxonDataset(key)
    td.setDataset(d)
    modifyTaxonDataset(td, metadata)
    db.em.persist(td)

    td
  }

  def updateExisting(metadata: Metadata) = {

    log.info("Updating existing dataset " + metadata.datasetKey)

    val td = db.repo.getTaxonDataset(metadata.datasetKey)
    modifyTaxonDataset(td, metadata)
    val d = td.getDataset
    modifyDataset(d, metadata)
    td
  }

  def modifyDataset(d: Dataset, m: Metadata) = {

    val providerOrganisation = db.repo.getOrganisation(m.datasetProviderName)
    val datasetUpdateFrequency = db.em.getReference(classOf[DatasetUpdateFrequency], "012")
    val datasetType = db.em.getReference(classOf[DatasetType], 'T')

    // certain fields are metadata, and we have to record when any changes
    var metadataChanged = false

    def setMetadata[T](value: T, getter: () => Any, setter: T => Unit) {
      metadataChanged = getter != value
      setter(value)
    }

    setMetadata(m.dataCaptureMethod, d.getDataCaptureMethod, d.setDataCaptureMethod)
    setMetadata(m.accessConstraints, d.getAccessConstraints, d.setAccessConstraints)
    setMetadata(m.dataQuality, d.getDataQuality, d.setDataQuality)
    setMetadata(m.datasetTitle, d.getTitle, d.setTitle)
    setMetadata(m.description, d.getDescription, d.setDescription)
    setMetadata(m.geographicCoverage, d.getGeographicalCoverage, d.setGeographicalCoverage)
    setMetadata(m.purpose, d.getPurpose, d.setPurpose)
    setMetadata(m.useConstraints, d.getUseConstraints, d.setUseConstraints)
    setMetadata(m.additionalInformation, d.getAdditionalInformation, d.setAdditionalInformation)
    setMetadata(m.temporalCoverage, d.getTemporalCoverage, d.setTemporalCoverage)

    d.setOrganisation(providerOrganisation) // not metadata
    d.setDatasetType(datasetType) // never changes, always 'T'
    d.setDateUploaded(Clock.nowUtc) // eventDate of this import
    d.setDatasetUpdateFrequency(datasetUpdateFrequency) // never changes, always '012'

    if (metadataChanged)
      d.setMetadataLastEdited(Clock.nowUtc)

    d
  }




  private def modifyTaxonDataset(td: TaxonDataset, m: Metadata) {


    val resolution = db.repo.getResolution(m.publicPrecision)

    td.setPublicResolution(resolution)

    // default .. to be read from extra metadata.
    // ...could be more columns like this
    td.setAllowRecordValidation(true)
  }
}
