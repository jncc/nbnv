package uk.org.nbn.nbnv.importer.ingestion

import scala.collection.JavaConversions._
import javax.persistence.{EntityTransaction, EntityManager}
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.importer.Options
import com.google.inject.Inject
import org.apache.log4j.Logger

/// Performs the interaction with the NBN core database.

class Ingester @Inject()(options: Options,
                         log: Logger,
                         em: EntityManager,
                         datasetIngester: DatasetIngester,
                         recordIngester: RecordIngester) {

  def ingest(archive: Archive, metadata: Metadata) {

    val t = em.getTransaction

    withEntityTransaction(t) {

      t.begin()

      // upsert dataset
      val dataset = datasetIngester.upsertDataset(metadata)
      em.flush()

      // upsert records
      for (record <- archive.iteratorRaw) {
        recordIngester.upsertRecord(new NbnRecord(record), dataset, metadata)
        em.flush()
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
      em.close() // todo: when we have more entity managers, where best to do this? is it even necessary?
    }
  }
}
