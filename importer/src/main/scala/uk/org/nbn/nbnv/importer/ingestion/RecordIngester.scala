
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.records.{PointDef, GridRefDef, BoundaryDef, NbnRecord}
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.BadDataException

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

  def insertRecord(record: NbnRecord, dataset: ImportTaxonDataset, metadata: Metadata) {

    log.info("Upserting record %s".format(record.key))
    
    val survey = db.repo.getImportSurvey(record.surveyKey getOrElse "1", dataset).get
    //val survey = surveyIngester.stageSurvey(record.surveyKey, dataset)
    val sample = db.repo.getImportSample(record.sampleKey getOrElse "1", survey).get
    //val sample = sampleIngester.stageSample(record.sampleKey, survey)
    val site = siteIngester.upsertSite(record.siteKey, record.siteName, dataset.getImportDataset)


    val feature = featureIngester.ensureFeature(record)

    val taxon = db.repo.getTaxon(record.taxonVersionKey)
    val dateType = db.repo.getDateType(record.dateType)
    val determiner = if (record.determiner.isDefined) {
        db.repo.getFirstImportRecorder(record.determiner.get)
      }
      else {
        None
      }

    val recorder = if (record.determiner.isDefined) {
        db.repo.getFirstImportRecorder(record.recorder.get)
      }
      else {
        None
      }

    val (startDate, endDate) = dateParser.parse(record.dateType, record.startDate, record.endDate)

    def update(o: ImportTaxonObservation) {

      o.setAbsenceRecord(record.absence)
      o.setDateStart(startDate getOrElse null)
      o.setDateEnd(endDate getOrElse null)
      o.setDateTypeKey(dateType.getKey)
      o.setDeterminerID(determiner getOrElse null)
      o.setFeatureID(feature.getId)
      o.setProviderKey(record.key)
      o.setRecorderID(recorder getOrElse null)
      o.setSampleID(sample)
      o.setSensitiveRecord(record.sensitiveOccurrence)
      o.setSiteID(site.orNull)
      o.setTaxonVersionKey(taxon.getTaxonVersionKey)
    }

    val observation = db.repo.getTaxonObservation(record.key, sample) match {
      case Some(o) => {
        update(o)
        o
      }
      case None => {
        val o = new ImportTaxonObservation
        update(o)
        db.em.persist(o)
        db.flushAndClear()
        o

      }
    }

    attributeIngester.ingestAttributes(record, observation, dataset)
    publicIngester.ingestPublic(observation, sample, metadata)
  }


}

