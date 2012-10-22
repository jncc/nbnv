package uk.org.nbn.nbnv.importer.ingestion

import scala.collection.JavaConversions._
import javax.persistence.{EntityTransaction, EntityManager}
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.importer.Options
import com.google.inject.Inject
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.data.Database
import com.google.common.base.Stopwatch

/// Performs the interaction with the NBN core database.

class Ingester @Inject()(options: Options,
                         log: Logger,
                         db: Database,
                         datasetIngester: DatasetIngester,
                         recordIngester: RecordIngester) {

  val watch = new Stopwatch()

  private def logProgress(i: Int) {
    log.info("Ingested %d records in %d seconds".format(i + 1, watch.elapsedMillis() / 1000))
    log.info("Ingestion average is %d milliseconds per record".format(watch.elapsedMillis() / (i + 1)))
  }

  def ingest(archive: Archive, metadata: Metadata) {

    val t = db.em.getTransaction

    withEntityTransaction(t) {

      t.begin()

      // upsert dataset
      val dataset = datasetIngester.upsertDataset(metadata)
      db.flushAndClear()

      watch.start()

      // upsert records in groups of 100, fully clearing the context to prevent an observed JPA slowdown
      for (group <- archive.iteratorRaw.zipWithIndex.grouped(100); (record, i) <- group) {

        recordIngester.upsertRecord(new NbnRecord(record), dataset, metadata)

        logProgress(i)
        db.flushAndClear()
      }

      if (options.whatIf) {
        log.info("Rolling back ingestion transaction (whatIf=true)")
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
