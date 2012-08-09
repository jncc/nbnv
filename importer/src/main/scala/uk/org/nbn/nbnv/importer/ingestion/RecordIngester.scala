
  package uk.org.nbn.nbnv.importer.ingestion

  import uk.org.nbn.nbnv.jpa.nbncore._
  import javax.persistence.EntityManager
  import org.apache.log4j.Logger
  import uk.org.nbn.nbnv.importer.records.NbnRecord
  import uk.org.nbn.nbnv.importer.data.Repository

  class RecordIngester(log:            Logger,
                       em: EntityManager,
                       surveyIngester: SurveyIngester,
                       sampleIngester: SampleIngester,
                       r:     Repository) {

    def upsertRecord(record: NbnRecord, dataset: TaxonDataset) {

      log.info("Upserting record %s".format(record.key))

      // todo: get existing if it's there
      //    val record = r.getTaxonObservation()

      val survey = surveyIngester.upsertSurvey(record.surveyKey, dataset)
      val sample = sampleIngester.upsertSample(record.sampleKey, survey)

      // assume site already exists until spec clarified
      val site = r.getSite(record.siteKey)

      // todo: TaxonObservation needs a feature, but what is a feature? we've forgotten
      val feature = r.getFeature(1)

      val taxon = r.getTaxon(record.taxonVersionKey)
      val dateType = r.getDateType(record.dateType)

      val determiner = ensureRecorder(record.determiner)
      val recorder = ensureRecorder(record.recorder)

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

    def ensureRecorder(name: String) = {
      r.getFirstRecorder(name) match {
        case Some(recorder) => recorder
        case _ => {
          new Recorder
          // todo: insert recorder
          // todo: will inserting a recorder mean it's available in subsequent queries? probably not!
        }
      }
    }
}
