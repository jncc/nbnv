package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.importer.utility._
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository, KeyGenerator}
import org.apache.log4j.Logger
import com.google.inject.Inject

class DatasetIngester @Inject()(log: Logger,
                                db: Database,
                                keyGenerator: KeyGenerator) {

  def stageDataset(metadata: Metadata): ImportTaxonDataset = {

    var key = ""

    if (metadata.datasetKey.isEmpty) {
      // Always inserting exactly 1 dataset
      key = "NewDst00"
    } else {
      key = metadata.datasetKey
    }

    log.info("Inserting new dataset " + key)

    val d = new ImportDataset(key)
    setDatasetValues(d, metadata)
    db.em.persist(d)

    val td = new ImportTaxonDataset(key)
    td.setImportDataset(d)
    setTaxonDatasetValues(td, metadata)
    db.em.persist(td)

    td
  }

  //todo: get rid of this stuff.
//  def upsertDataset(metadata: Metadata): TaxonDataset = {
//
//    // we're only dealing with *Taxon* Datasets at the moment
//
//    // when there's no key given, insert a new dataset (and generate a new key)
//    // when there is one, find it and update it (otherwise throw)
//    if (metadata.datasetKey.isEmpty) {
//      insertNew(metadata)
//    }
//    else {
//      // we have no way to say that a particular record should be deleted,
//      // so delete all the records - we will import them all again
//      //todo: move this to be the last step in the import before running the sproc from the staging tables
//      db.repo.deleteTaxonObservationsAndRelatedRecords(metadata.datasetKey)
//
//      updateExisting(metadata)
//    }
//
//  }

//  def insertNew(metadata: Metadata) = {
//
//    // generate a new key and a new dataset
//    val key = keyGenerator.nextTaxonDatasetKey
//
//    log.info("Inserting new dataset " + key)
//
//    val d = new Dataset(key)
//    setDatasetValues(d, metadata)
//    db.em.persist(d)
//
//    // deal with the table-per-class inheritance model (TaxonDataset has-a Dataset)
//    val td = new TaxonDataset(key)
//    td.setDataset(d)
//    setTaxonDatasetValues(td, metadata)
//    db.em.persist(td)
//
//    td
//  }
//
//  def updateExisting(metadata: Metadata) = {
//
//    log.info("Updating existing dataset " + metadata.datasetKey)
//
//    val td = db.repo.getTaxonDataset(metadata.datasetKey)
//    setTaxonDatasetValues(td, metadata)
//    val d = td.getDataset
//    setDatasetValues(d, metadata)
//    td
//  }

  def setDatasetValues(d: ImportDataset, m: Metadata) = {

    val providerOrganisation = db.repo.getOrganisation(m.datasetProviderName)
//    val datasetUpdateFrequency = db.em.getReference(classOf[DatasetUpdateFrequency], "012")
//    val datasetType = db.em.getReference(classOf[DatasetType], 'T')

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

    d.setProviderOrganisationKey(providerOrganisation.getId) // not metadata
//    d.setOrganisation(providerOrganisation)
    d.setDatasetTypeKey('T') // never changes, always 'T'
//    d.setDatasetType(datasetType)
    d.setDateUploaded(Clock.nowUtc) // eventDate of this import
    d.setUpdateFrequencyCode("012") // never changes, always '012'
//    d.setDatasetUpdateFrequency(datasetUpdateFrequency) // never changes, always '012'

    if (metadataChanged)
      d.setMetadataLastEdited(Clock.nowUtc)

    d
  }

  def setTaxonDatasetValues(td: ImportTaxonDataset, m: Metadata) {

    val resolutionId = db.repo.getResolution(m.publicResolution).getId
    td.setPublicResolutionID(resolutionId)
//    td.setPublicResolution(resolution)

    // default .. to be read from extra metadata.
    // ...could be more columns like this
    td.setAllowRecordValidation(true)
  }
}
