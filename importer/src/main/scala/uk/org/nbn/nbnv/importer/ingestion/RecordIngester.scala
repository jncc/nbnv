
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.data.Repository
import com.google.inject.Inject

class RecordIngester @Inject()(log: Logger,
                               em: EntityManager,
                               surveyIngester: SurveyIngester,
                               sampleIngester: SampleIngester,
                               siteIngester: SiteIngester,
                               recorderIngester: RecorderIngester,
                               attributeIngester: AttributeIngester,
                               repo: Repository) {

  def upsertRecord(record: NbnRecord, dataset: TaxonDataset) {

    log.info("Upserting record %s".format(record.key))

    val survey = surveyIngester.upsertSurvey(record.surveyKey, dataset)
    val sample = sampleIngester.upsertSample(record.sampleKey, survey)
    val site = siteIngester.upsertSite(record.siteKey, record.siteName, dataset.getDataset)
    // todo: TaxonObservation needs a feature - need to get c# code from paul
    val feature = repo.getFeature(1)
    val taxon = repo.getTaxon(record.taxonVersionKey)
    val dateType = repo.getDateType(record.dateType)
    val determiner = recorderIngester.ensureRecorder(record.determiner)
    val recorder = recorderIngester.ensureRecorder(record.recorder)

    def update(o: TaxonObservation) {

      o.setAbsenceRecord(record.absence)
      o.setDateStart(record.startDate)
      o.setDateEnd(record.endDate)
      o.setDateType(dateType)
      o.setDeterminerID(determiner)
      o.setFeatureID(feature)
      o.setObservationKey(record.key)
      o.setRecorderID(recorder)
      o.setSampleID(sample)
      o.setSensitiveRecord(record.sensitiveOccurrence)
      o.setSiteID(site.orNull)
      o.setTaxonVersionKey(taxon)
    }

    val observation = repo.getTaxonObservation(record.key, sample) match {
      case Some(o) => {
        update(o)
        o
      }
      case None => {
        val o = new TaxonObservation
        update(o)
        em.persist(o)
        em.flush()
        o
      }
    }

    attributeIngester.ingestAttributes(record, observation, dataset)
  }
}
