package uk.org.nbn.nbnv.importer.ingestion

import scala.collection.JavaConversions._
import javax.persistence.{EntityTransaction, EntityManager}
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.gbif.dwc.text.Archive
import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.importer.{WhatIfException, Options}
import com.google.inject.Inject
import org.apache.log4j.Logger
import com.google.inject.persist.{Transactional, PersistService}

/// Performs the interaction with the NBN core database.
class Ingester @Inject()(options: Options,
                         log: Logger,
                         em: EntityManager,
                         datasetIngester: DatasetIngester,
                         recordIngester: RecordIngester) {

  @Transactional()
  def ingest(archive: Archive, metadata: Metadata) {

    // upsert dataset
    val dataset = datasetIngester.upsertDataset(metadata)
    em.flush()

    // upsert records
    for (record <- archive.iteratorRaw) {
      recordIngester.upsertRecord(new NbnRecord(record), dataset)
      em.flush()
    }

    if (options.whatIf) {
      throw new WhatIfException("Rolling back ingestion transaction (whatIf=true)")
    }

    log.info("Completed ingestion")
  }
}
