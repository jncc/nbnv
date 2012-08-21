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
                                repository: Repository,
                                organisationIngester: OrganisationIngester) {

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

  private def insertNew(metadata: Metadata) = {

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

  private def updateExisting(metadata: Metadata) = {

    log.info("Updating existing dataset " + metadata.datasetKey)

    val td = repository.getTaxonDataset(metadata.datasetKey)
    modifyTaxonDataset(td, metadata)
    val d = td.getDataset
    modifyDataset(d, metadata)
    td
  }

  private def modifyDataset(d: Dataset, m: Metadata) = {

    val provider = organisationIngester.ensureOrganisation(m.datasetProviderName)
    val datasetUpdateFrequency = em.getReference(classOf[DatasetUpdateFrequency], "012")
    val datasetType = em.getReference(classOf[DatasetType], 'T')

    d.setAccessConstraints(m.accessConstraints)
    d.setDataCaptureMethod(m.dataCaptureMethod)
    d.setDatasetProvider(provider) //* not metadata
    d.setDataQuality(m.dataQuality)
    d.setDatasetTitle(m.datasetTitle)
    //d.setDatasetTypeKey(datasetType) //* should actually never change
    d.setDateUploaded(Clock.nowUtc) //* not metadata
    d.setDescription(m.description)
    d.setGeographicalCoverage(m.geographicCoverage)
    d.setPurpose(m.purpose)
    d.setUseConstraints(m.useConstraints)
    d.setUpdateFrequency(datasetUpdateFrequency)
    d.setAdditionalInformation(m.additionalInformation)
    d.setTemporalCoverage(m.temporalCoverage)

    // todo: only set if any of the other fields (apart from those marked *) have changed!
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
