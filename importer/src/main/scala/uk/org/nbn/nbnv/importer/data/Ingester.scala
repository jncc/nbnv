package uk.org.nbn.nbnv.importer.data

import scala.collection.JavaConversions._
import javax.persistence.{EntityTransaction, EntityManager}
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.metadata.Metadata

/// Performs the interaction with the NBN core database.
class Ingester(entityManager:   EntityManager,
               datasetIngester: DatasetIngester,
               recordIngester:  RecordIngester) {

  def ingest(archive: Archive, metadata: Metadata) {

    val t = entityManager.getTransaction

    withEntityTransaction(t) {

      t.begin()

      // upsert dataset
      val dataset = datasetIngester.upsertDataset(metadata)

      // upsert records
      for (record <- archive.iteratorRaw) {
        recordIngester.upsertRecord(new NbnRecord(record), dataset)
      }

      t.commit()
    }
  }

  def withEntityTransaction(t: EntityTransaction)(f: => Unit) {
    try { f }
    catch {
      case e: Exception => {
        if (t != null && t.isActive) t.rollback()
        throw(e)
      }
    }
    finally {
      entityManager.close()
    }
  }
}
