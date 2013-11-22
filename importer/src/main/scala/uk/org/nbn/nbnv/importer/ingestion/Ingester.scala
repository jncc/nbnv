package uk.org.nbn.nbnv.importer.ingestion

import scala.collection.JavaConversions._
import javax.persistence.EntityTransaction
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.importer.{Mode, Target, Options}
import com.google.inject.Inject
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.data.Database
import com.google.common.base.Stopwatch
import uk.org.nbn.nbnv.jpa.nbncore.{ImportTaxonDataset, TaxonDataset}
import com.sun.jersey.api.client.ClientResponse
import javax.ws.rs.core.MediaType
import uk.org.nbn.nbnv.importer.jersey.WebApi

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
                         featureIngester: FeatureIngester,
                         api: WebApi) {

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
    if (options.mode == Mode.full) {
      log.info("Deleting existing records...")
      //Clear down the taxon observations
      db.repo.deleteTaxonObservationsAndRelatedRecords(metadata.datasetKey)
    }

    log.info("Importing records...")

    val i = watch.elapsedMillis()
    db.repo.importTaxonObservationsAndRelatedRecords()
    log.info("Imported records in %d seconds".format((watch.elapsedMillis() - i) / 1000))

  }


  def ingest(archive: Archive, metadata: Metadata) {

    def finaliseTransaction(t: EntityTransaction) {
      if (options.target < Target.commit) {
        log.info("Rolling back ingestion transaction")
        t.rollback()
      }
      else {
        log.info("Committing ingestion transaction")
        t.commit()
      }
    }

    //Clear down staging tables
    val t1: EntityTransaction = db.em.getTransaction

    withEntityTransaction(t1) {

      t1.begin()

      db.repo.clearImportStagingTables()

      // upsert dataset
      val dataset = datasetIngester.stageDataset(metadata)
      db.flushAndClear()

      watch.start()
      //upnsert surveys
      stageSurveys(archive, dataset)

      //upnsert samples
      stageSamples(archive, dataset)
      
      //insert recorders & determiners
      stageRecorders(archive)

      // insert records
      upsertRecords(archive, dataset, metadata)

      t1.commit()
    }
    log.info("Step 1 Complete: Ingested data into import staging tables")

    val t2 = db.em.getTransaction

    //Importing data into database
    withEntityTransaction(t2) {

      t2.begin()

      finaliseImport(metadata)

      finaliseTransaction(t2)

    }
    log.info("Step 2 Complete: Imported data into core tables")

    if (options.target >= Target.commit && ! metadata.datasetKey.isEmpty()) {
      try {
        api.resetDatasetAccess(metadata.datasetKey)

        log.info("Step 3 Complete: Called API endpoints to reset dataset access")
      }
      catch {
        case e: Throwable => {
          log.warn("Step 3 Failed: An error occured when callling the API to rest access for dataset %s".format(metadata.datasetKey), e)
        }
      }
    }

    db.em.close()
  }

  def withEntityTransaction(t: EntityTransaction)(f: => Unit) {
    try {
      f
    }
    catch {
      case e: Throwable => {
        if (t != null && t.isActive) t.rollback()
        db.em.close()
        throw (e)
      }
    }
  }
}
