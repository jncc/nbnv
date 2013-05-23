
package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.jpa.nbncore._
import javax.persistence.EntityManager
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.metadata.Metadata

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
    
    val survey = db.repo.getSurvey(record.surveyKey getOrElse "1", dataset).get
    //val survey = surveyIngester.upsertSurvey(record.surveyKey, dataset)
    val sample = db.repo.getSample(record.sampleKey getOrElse "1", survey).get
    //val sample = sampleIngester.upsertSample(record.sampleKey, survey)
    val site = if (record.siteKey.isDefined) {
    		db.repo.getSite(record.siteKey.get, dataset.getDataset)
    	}
    	else {
    		None
    	}
    //val site = siteIngester.upsertSite(record.siteKey, record.siteName, dataset.getDataset)
    val feature = featureIngester.getFeature(record) 
    val taxon = db.repo.getTaxon(record.taxonVersionKey)
    val dateType = db.repo.getDateType(record.dateType)
    val determiner = if (record.determiner.isDefined) {
        db.repo.getFirstRecorder(record.determiner.get)
      }
      else {
        None
      }

    val recorder = if (record.determiner.isDefined) {
        db.repo.getFirstRecorder(record.recorder.get)
      }
      else {
        None
      }

    val (startDate, endDate) = dateParser.parse(record.dateType, record.startDate, record.endDate)

    def update(o: TaxonObservation) {

      o.setAbsenceRecord(record.absence)
      o.setDateStart(startDate getOrElse null)
      o.setDateEnd(endDate getOrElse null)
      o.setDateType(dateType)
      o.setDeterminer(determiner getOrElse null)
      o.setFeature(feature)
      o.setProviderKey(record.key)
      o.setRecorder(recorder getOrElse null)
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

    attributeIngester.ingestAttributes(record, observation, dataset)
    publicIngester.ingestPublic(observation, sample, metadata)
  }
  
//  public getFeature(record: NbnRecord)
//  {
//
//    case value: GridRefDef  => ensureGridRefFeature(value)
//    case value: BoundaryDef => ensureSiteBoundaryFeature(value)
//    case value: PointDef    => ensureGridRefFeatureByCoordinate(value)
//  }

}

