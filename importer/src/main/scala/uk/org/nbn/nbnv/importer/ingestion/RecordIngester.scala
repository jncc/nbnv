
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

    val sample = db.repo.getImportSample(record.sampleKey getOrElse "1", survey).get

    val site = siteIngester.upsertSite(record.siteKey, record.siteName, dataset.getImportDataset)

    val feature = featureIngester.ensureFeature(record)

    val determiner = if (record.determiner.isDefined) {
        db.repo.getFirstImportRecorder(record.determiner.get)
      }
      else {
        None
      }

    val recorder = if (record.recorder.isDefined) {
        db.repo.getFirstImportRecorder(record.recorder.get)
      }
      else {
        None
      }

    val (startDate, endDate) = dateParser.parse(record.dateType.get, record.startDate, record.endDate)

    def update(o: ImportTaxonObservation) {

      o.setAbsenceRecord(record.absence)
      o.setDateStart(startDate getOrElse null)
      o.setDateEnd(endDate getOrElse null)
      o.setDateTypeKey(record.dateType.get) //confirmed by NBNV-78 validator
      o.setDeterminerID(determiner getOrElse null)
      o.setFeatureID(feature.getId)
      o.setProviderKey(record.key)
      o.setRecorderID(recorder getOrElse null)
      o.setSampleID(sample)
      o.setSensitiveRecord(record.sensitiveOccurrence)
      o.setSiteID(site.orNull)
      o.setTaxonVersionKey(record.taxonVersionKey.get)
    }

    val observation = new ImportTaxonObservation
    update(observation)
    db.em.persist(observation)
    db.em.flush()
//    db.flushAndClear()

    attributeIngester.ingestAttributes(record, observation, dataset)

    if (metadata.publicPrecision != 0) publicIngester.ingestPublic(observation, sample, metadata)
  }


}

