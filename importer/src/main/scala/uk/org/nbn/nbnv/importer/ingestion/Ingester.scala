package uk.org.nbn.nbnv.importer.ingestion

import scala.collection.JavaConversions._
import javax.persistence.{EntityTransaction, EntityManager}
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.importer.Options
import com.google.inject.Inject

/// Performs the interaction with the NBN core database.

class Ingester @Inject()(options: Options,
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
        recordIngester.upsertRecord(new NbnRecord(record), dataset)
        em.flush()
        // todo: set no caching for records?
      }

      if (options.whatIf)
        t.rollback()
      else
        t.commit()
    }
  }

  def withEntityTransaction(t: EntityTransaction)(f: => Unit) {
    try {
      f
    }
    catch {
      case e: Exception => {
        if (t != null && t.isActive) t.rollback()
        throw (e)
      }
    }
    finally {
      em.close()
    }
  }
}
