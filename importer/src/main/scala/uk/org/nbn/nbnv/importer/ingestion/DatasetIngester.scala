package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.importer.utility._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, CoreRepository, KeyGenerator}
import org.apache.log4j.Logger
import com.google.inject.Inject

class DatasetIngester @Inject()(log: Logger,
                                db: Database,
                                keyGenerator: KeyGenerator) {

  def upsertDataset(metadata: Metadata): TaxonDataset = {

    // we're only dealing with *Taxon* Datasets at the moment

    // when there's no key given, insert a new dataset (and generate a new key)
    // when there is one, find it and update it (otherwise throw)
    if (metadata.datasetKey.isEmpty) {
      insertNew(metadata)
    }
    else {
      // we have no way to say that a particular record should be deleted,
      // so delete all the records - we will import them all again
      db.coreRepo.deleteTaxonObservationsAndRelatedRecords(metadata.datasetKey)

      updateExisting(metadata)
    }
  }

  def insertNew(metadata: Metadata) = {

    // generate a new key and a new dataset
    val key = keyGenerator.nextTaxonDatasetKey

    log.info("Inserting new dataset " + key)

    val d = new Dataset(key)
    setDatasetValues(d, metadata)
    db.em.persist(d)

    // deal with the table-per-class inheritance model (TaxonDataset has-a Dataset)
    val td = new TaxonDataset(key)
    td.setDataset(d)
    setTaxonDatasetValues(td, metadata)
    db.em.persist(td)

    td
  }

  def updateExisting(metadata: Metadata) = {

    log.info("Updating existing dataset " + metadata.datasetKey)

    val td = db.coreRepo.getTaxonDataset(metadata.datasetKey)
    setTaxonDatasetValues(td, metadata)
    val d = td.getDataset
    setDatasetValues(d, metadata)
    td
  }

  def setDatasetValues(d: Dataset, m: Metadata) = {

    // todo get Id
    val providerOrganisation = db.coreRepo.getOrganisation(m.datasetProviderName)

    //todo make static strings
    val datasetUpdateFrequency = db.em.getReference(classOf[DatasetUpdateFrequency], "012")
    val datasetType = db.em.getReference(classOf[DatasetType], 'T')

    // we have to record when certain fields change
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

  def setTaxonDatasetValues(td: TaxonDataset, m: Metadata) {

    val resolution = db.coreRepo.getResolution(m.publicResolution)
    td.setPublicResolution(resolution)

    // default .. to be read from extra metadata.
    // ...could be more columns like this
    td.setAllowRecordValidation(true)
  }
}
