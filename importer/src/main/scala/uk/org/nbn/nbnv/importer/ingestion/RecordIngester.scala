
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.data.Repository

class RecordIngester(log: Logger,
                     em: EntityManager,
                     surveyIngester: SurveyIngester,
                     sampleIngester: SampleIngester,
                     recorderIngester: RecorderIngester,
                     r: Repository) {

  def upsertRecord(record: NbnRecord, dataset: TaxonDataset) {

    log.info("Upserting record %s".format(record.key))

    val survey = surveyIngester.upsertSurvey(record.surveyKey, dataset)
    val sample = sampleIngester.upsertSample(record.sampleKey, survey)

    //em.flush() // get the new ids from the database

    val observation = r.getTaxonObservation(record.key, sample)

    observation match {
      case Some(o) => {
        update(o)
        o
      }
      case None => {
        val o = new TaxonObservation()
        update(o)
        em.persist(o)
        o
      }
    }

    def update(o: TaxonObservation) {

      // todo: leave until schema change is done - sites are now scoped to datasets
      val site = r.getSite(record.siteKey)

      // todo: TaxonObservation needs a feature
      // need to get c# code ... paul will get back to us
      val feature = r.getFeature(1)

      val taxon = r.getTaxon(record.taxonVersionKey)
      val dateType = r.getDateType(record.dateType)

      val determiner = recorderIngester.ensureRecorder(record.determiner)
      val recorder = recorderIngester.ensureRecorder(record.recorder)

      o.setAbsenceRecord(false)
      o.setDateStart(record.startDate)
      o.setDateEnd(record.endDate)
      o.setDateType(dateType)
      o.setDeterminerID(determiner)
      o.setFeatureID(feature)
      o.setObservationKey(record.key)
      o.setRecorderID(recorder)
      o.setSampleID(sample)
      o.setSensitiveRecord(false)
      o.setSiteID(site)
      o.setTaxonVersionKey(taxon)
    }
  }
}
