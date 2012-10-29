
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import com.google.inject.Inject
import uk.org.nbn.nbnv.metadata.Metadata

class RecordIngester @Inject()(log: Logger,
                               db: Database,
                               surveyIngester: SurveyIngester,
                               sampleIngester: SampleIngester,
                               siteIngester: SiteIngester,
                               recorderIngester: RecorderIngester,
                               attributeIngester: AttributeIngester,
                               featureIngester: FeatureIngester,
                               publicIngester: PublicIngester,
                               dateParser: NbnDateParser) {

  def insertRecord(record: NbnRecord, dataset: TaxonDataset, metadata: Metadata) {

    log.info("Upserting record %s".format(record.key))

    val survey = surveyIngester.upsertSurvey(record.surveyKey, dataset)
    val sample = sampleIngester.upsertSample(record.sampleKey, survey)
    val site = siteIngester.upsertSite(record.siteKey, record.siteName, dataset.getDataset)
    val feature = featureIngester.ensureFeature(record)
    val taxon = db.repo.getTaxon(record.taxonVersionKey)
    val dateType = db.repo.getDateType(record.dateType)
    val determiner = recorderIngester.ensureRecorder(record.determiner)
    val recorder = recorderIngester.ensureRecorder(record.recorder)

    val (startDate, endDate) = dateParser.parse(record.dateType, record.startDate, record.endDate)

    def update(o: TaxonObservation) {

      o.setAbsenceRecord(record.absence)
      o.setDateStart(startDate.getOrElse(null))
      o.setDateEnd(endDate.getOrElse(null))
      o.setDateType(dateType)
      o.setDeterminer(determiner)
      o.setFeature(feature)
      o.setProviderKey(record.key)
      o.setRecorder(recorder)
      o.setSample(sample)
      o.setSensitiveRecord(record.sensitiveOccurrence)
      o.setSite(site.orNull)
      o.setTaxon(taxon)
    }

    val observation = db.repo.getTaxonObservation(record.key, sample) match {
      case Some(o) => {
        update(o)
        o
      }
      case None => {
        val o = new TaxonObservation
        update(o)
        db.em.persist(o)
        o
      }
    }

    db.em.flush()

    attributeIngester.ingestAttributes(record, observation, dataset)
    publicIngester.ingestPublic(observation, sample, metadata)

    db.em.flush()
  }
}
