package uk.org.nbn.nbnv.importer.ingestion

import scala.collection.JavaConversions._
import javax.persistence.EntityTransaction
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.metadata.{Mode, Metadata}
import uk.org.nbn.nbnv.importer.{Target, Options}
import com.google.inject.Inject
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.data.Database
import com.google.common.base.Stopwatch
import uk.org.nbn.nbnv.jpa.nbncore.ImportTaxonDataset
import uk.org.nbn.nbnv.importer.jersey.WebApi
import uk.org.nbn.nbnv.importer.archive.Archive

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
    for ((record, i) <- archive.records.zipWithIndex) {
      surveyIngester.stageSurvey(record.surveyKey, dataset)

      logProgress(i)
    }

    //Save all the surveys
    db.flushAndClear()
  }


  def stageSamples(archive: Archive, dataset: ImportTaxonDataset) {
    log.debug("Ingesting samples...")
    for ((record, i) <- archive.records.zipWithIndex) {
      val survey = db.repo.getImportSurvey((record.surveyKey getOrElse "1"),dataset )
      sampleIngester.stageSample(record.sampleKey, survey.get)

      logProgress(i)
    }

    //Save all the surveys
    db.flushAndClear()
  }
  
  def stageRecorders(archive: Archive) {
    log.debug("Ingesting recorders...")
    for ((record, i) <- archive.records.zipWithIndex) {
      recorderIngester.ensureRecorder(record.determiner)
      recorderIngester.ensureRecorder(record.recorder)

      logProgress(i)
    }
   
    db.flushAndClear()
  }
  
  def stageRecords(archive: Archive, dataset: ImportTaxonDataset, metadata: Metadata) {
    log.debug("Ingesting records...")
     for ((record, i) <- archive.records.zipWithIndex) {

        recordIngester.insertRecord(record, dataset, metadata)
        logProgress(i)

        // every 100 records, fully clear the data context to prevent observed JPA slowdown
        // (using Seq.grouped eats the entire GBIF iterator for reason!)
        if (i % options.flush == 0) {
          db.flushAndClear()
        }
      }
  }

  def finaliseImport(metadata: Metadata) : String =
  {
    if (metadata.importType.get == Mode.upsert) {
      log.info("Deleting existing records...")
      //Clear down the taxon observations
      db.repo.deleteTaxonObservationsAndRelatedRecords(metadata.datasetKey)
    }

    log.info("Importing records...")

    val i = watch.elapsedMillis()
    val datasetKey = db.repo.importTaxonObservationsAndRelatedRecords()
    log.info("Imported records in %d seconds".format((watch.elapsedMillis() - i) / 1000))
    datasetKey
  }


  def checkAndSetDatasetAsPublic(metadata: Metadata, datasetKey: String) {
    if (metadata.publicPrecision == 100 &&
      metadata.recorderAndDeterminerArePublic &&
      metadata.attributesArePublic) {

      db.repo.setDatasetPublic(datasetKey)
    }
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
      stageRecords(archive, dataset, metadata)

      t1.commit()
    }
    log.info("Step 1 Complete: Ingested data into import staging tables")

    val t2 = db.em.getTransaction

    var datasetKey = ""
    //Importing data into database
    withEntityTransaction(t2) {

      t2.begin()

      datasetKey = finaliseImport(metadata)

      checkAndSetDatasetAsPublic(metadata, datasetKey)

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

    if (datasetKey != "") {
      val modeString = metadata.importType match {
        case Some(Mode.upsert) => "new or update (upsert)"
        case Some(Mode.append) => "append"
        case _ => "something went wrong"
      }

      log.info("Processed dataset key: %s, Import type: %s".format(datasetKey, modeString))
    }
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
