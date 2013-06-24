package uk.org.nbn.nbnv.importer.ingestion

import scala.collection.JavaConversions._
import javax.persistence.EntityTransaction
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.{Target, Options}
import com.google.inject.Inject
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.data.Database
import com.google.common.base.Stopwatch
import uk.org.nbn.nbnv.jpa.nbncore.{ImportTaxonDataset, TaxonDataset}

/// Performs the interaction with the NBN core database.

class Ingester @Inject()(options: Options,
                         log: Logger,
                         db: Database,
                         datasetIngester: DatasetIngester,
                         recordIngester: RecordIngester,
                         surveyIngester: SurveyIngester,
                         sampleIngester: SampleIngester,
                         siteIngester: SiteIngester,
                         recorderIngester: RecorderIngester,
                         featureIngester: FeatureIngester) {

  val watch = new Stopwatch()

  private def logProgress(i: Int) {
    log.info("Ingested %d records in %d seconds".format(i + 1, watch.elapsedMillis() / 1000))
    log.info("Ingestion average is %d milliseconds per record".format(watch.elapsedMillis() / (i + 1)))
  }

  def stageSurveys(archive: Archive, dataset: ImportTaxonDataset) {
    log.debug("Ingesting surveys...")
    for ((record, i) <- archive.iteratorRaw.zipWithIndex) {
      val rec = new NbnRecord(record)
      surveyIngester.stageSurvey(rec.surveyKey, dataset)

      logProgress(i)
    }

    //Save all the surveys
    db.flushAndClear()
  }


  def stageSamples(archive: Archive, dataset: ImportTaxonDataset) {
    log.debug("Ingesting samples...")
    for ((record, i) <- archive.iteratorRaw.zipWithIndex) {
      val rec = new NbnRecord(record)
      val survey = db.repo.getImportSurvey((rec.surveyKey getOrElse "1"),dataset )
      sampleIngester.stageSample(rec.sampleKey, survey.get)

      logProgress(i)
    }

    //Save all the surveys
    db.flushAndClear()
  }

  def stageSites(archive: Archive, dataset: ImportTaxonDataset) {
    log.debug("Ingesting sites...")
    for ((record, i) <- archive.iteratorRaw.zipWithIndex) {
      val rec = new NbnRecord(record)
      siteIngester.stageSite(rec.siteKey, rec.siteName, dataset.getImportDataset)

      logProgress(i)
    }
    
    db.flushAndClear()
  }
  
  def stageRecorders(archive: Archive) {
    log.debug("Ingesting recorders...")
    for ((record, i) <- archive.iteratorRaw.zipWithIndex) {
      val rec = new NbnRecord(record)
      recorderIngester.ensureRecorder(rec.determiner)
      recorderIngester.ensureRecorder(rec.recorder)

      logProgress(i)
    }
   
    db.flushAndClear()
  }
  
  def upsertGridSquareFeatures(archive: Archive) {
    log.debug("Ingesting grid squares...")
    for ((record, i) <- archive.iteratorRaw.zipWithIndex) {
      val rec = new NbnRecord(record)
      
      featureIngester.ensureGridSquareFeature(rec)

      logProgress(i)
    }
    
    db.flushAndClear()
  }
  
  def upsertRecords(archive: Archive, dataset: ImportTaxonDataset, metadata: Metadata) {
    log.debug("Ingesting records...")
     for ((record, i) <- archive.iteratorRaw.zipWithIndex) {

        recordIngester.insertRecord(new NbnRecord(record), dataset, metadata)
        logProgress(i)

        // every 100 records, fully clear the data context to prevent observed JPA slowdown
        // (using Seq.grouped eats the entire GBIF iterator for reason!)
        if (i % 100 == 99) {
          db.flushAndClear()
        }
      }
  }

  def finaliseImport(metadata: Metadata)
  {
    log.info("Deleting existing records...")
    //Clear down the taxon observations
    db.repo.deleteTaxonObservationsAndRelatedRecords(metadata.datasetKey)

    log.info("Importing records...")

    val i = watch.elapsedMillis()
    db.repo.importTaxonObservationsAndRelatedRecords()
    log.info("Imported records in %d seconds".format((watch.elapsedMillis() - i) / 1000))
  }

  
  def ingest(archive: Archive, metadata: Metadata) {

    val t = db.em.getTransaction

    withEntityTransaction(t) {

      t.begin()

      // Clear down importer tables
      db.repo.clearImportStagingTables()

      // upsert dataset
      val dataset = datasetIngester.stageDataset(metadata)
      db.flushAndClear()

      watch.start()
      //upnsert surveys
      stageSurveys(archive, dataset)

      //upnsert samples
      stageSamples(archive, dataset)
      
      //upnsert sites
      stageSites(archive, dataset)
      
      //insert recorders & determiners
      stageRecorders(archive)
     
      //insert grid square features
      upsertGridSquareFeatures(archive)

      // insert records
      upsertRecords(archive, dataset, metadata)

      //Import data into main database.
      finaliseImport(metadata)

      if (options.target < Target.commit) {
        log.info("Rolling back ingestion transaction")
        t.rollback()
      }
      else {
        log.info("Committing ingestion transaction")
        t.commit()
      }
    }
  }

  def withEntityTransaction(t: EntityTransaction)(f: => Unit) {
    try {
      f
    }
    catch {
      case e: Throwable => {
        if (t != null && t.isActive) t.rollback()
        throw (e)
      }
    }
    finally {
      db.em.close()
    }
  }
}
