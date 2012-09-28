package uk.org.nbn.nbnv.importer.ingestion

import com.google.inject.Inject
import org.apache.log4j.Logger
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.spatial.GridSquareInfoFactory
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.jpa.nbncore.{Sample, TaxonObservationPublic}

/// Creates, updates or deletes the TaxonObservationPublic record appropriately.

class PublicIngester @Inject()(log: Logger, em: EntityManager, repo: Repository, gridSquareInfoFactory: GridSquareInfoFactory) {

  def ingestPublic(record: NbnRecord, sample: Sample, metadata: Metadata) {

    def update(o: TaxonObservationPublic) {

      // set the siteID to that of the record, or leave it null
      // if (metadata.siteIsPublic) {
      //   o.setSiteID(record.getSiteID)
      // }
      //
      // set the recorder & determiner IDs to those of the record, or leave them null
      // fix up the feature ID
      // if
//         o.setDeterminerID(determiner)
//         o.setRecorderID(recorder)
//         o.setFeatureID(feature)
    }

    repo.getTaxonObservationPublic(record.key, sample) match {
      case Some(o) => {
        // delete the public record if it's now sensitive
        // if (metadata.isSensitive) {
          em.remove(o)
        // } else {
          update(o)
        // }
      }
      case None => {
        // create the public record if it's not sensitive
        // if (!metadata.isSensitive) {
          val o = new TaxonObservationPublic
          update(o)
          em.persist(o)
        // }
      }
    }
  }
}
