/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.data.SiteRepository

class RecordIngester(log: Logger, em: EntityManager, surveyIngester: SurveyIngester, sampleIngester: SampleIngester, siteRepository: SiteRepository) {

  def upsertRecord(record: NbnRecord, dataset: TaxonDataset) {

    log.info("Upserting record %s".format(record.key))

    // todo: get existing if it's there

    val survey = surveyIngester.upsertSurvey(record.surveyKey, dataset)
    val sample = sampleIngester.upsertSample(record.sampleKey, survey)

    // sites are a bit up in the air - assume it already exists for now
    val site = siteRepository.getSite(record.siteKey)

    // todo: taxonobservation needs a feature, but what is a feature? we've forgotten
    val feature = em.find(classOf[Feature], 1)
    val taxon = em.getReference(classOf[Taxon], record.taxonVersionKey)
    val dateType = em.getReference(classOf[DateType], record.dateType)

    // todo: look up by value
    val determiner = em.find(classOf[Recorder], 1)
    val recorder = em.find(classOf[Recorder], 1)

    // now we've got the  related entities, we can upsert the actual record
    val o = new TaxonObservation()

    o.setAbsenceRecord(false)
    o.setDateStart(record.startDate) // todo?
    o.setDateEnd(record.startDate) // todo??
    o.setDateType(dateType)
    o.setDeterminerID(determiner)
    o.setFeatureID(feature)
    //o.setObservationID()
    o.setObservationKey(record.key)
    o.setRecorderID(recorder)
    o.setSampleID(sample)
    o.setSensitiveRecord(false)
    o.setSiteID(site)
    o.setTaxonVersionKey(taxon)

    em.merge(o)
  }
}
