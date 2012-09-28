package uk.org.nbn.nbnv.importer.ingestion

import com.google.inject.Inject
import org.apache.log4j.Logger
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.spatial.GridSquareInfoFactory
import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.jpa.nbncore.{TaxonObservation, Sample, TaxonObservationPublic}

/// Creates, updates or deletes the TaxonObservationPublic record appropriately.

class PublicIngester @Inject()(log: Logger, em: EntityManager, repo: Repository, gridSquareInfoFactory: GridSquareInfoFactory) {

  def ingestPublic(o: TaxonObservation, sample: Sample, metadata: Metadata) {

    def update(p: TaxonObservationPublic) {

      // set the fields which are always the same in the public record
      p.setSampleID(o.getSampleID)
      p.setObservationKey(o.getObservationKey)

      // set the siteID to that of the observation, or leave it null
      // if (metadata.siteIsPublic) {
      //   p.setSiteID(o.getSiteID)
      // }
      //
      // set the recorder & determiner IDs to those of the observation, or leave them null
      // if (metadata.recorderAndDeterminerArePublic) {
      //   p.setDeterminerID(o.getDeterminerID)
      //   p.setRecorderID(o.getRecorderID)
      // }
      //
      // fix up the feature ID
//         p.setFeatureID(feature)
    }

    repo.getTaxonObservationPublic(o.getObservationID) match {
      case Some(p) => {
        // delete the public record if it's now sensitive
        // if (metadata.isSensitive) {
        log.info("Deleting public record...")
        em.remove(p)
        // } else {
        log.info("Updating public record...")
        update(p)
        // }
      }
      case None => {
        // create the public record if it's not sensitive
        // if (!metadata.isSensitive) {
          log.info("Creating public record...")
          val p = new TaxonObservationPublic
          update(p)
          em.persist(p)
        // } else {
          log.info("Not creating public record...")
        // }
      }
    }
  }
}
