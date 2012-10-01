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
      //p.setSampleID(o.getSampleID) - gone

      p.setTaxonObservationID(o.getId)

      // set the site to that of the observation, or set it null
      // val site = if (metadata.siteIsPublic) o.getSiteID else null
      // p.setSiteID(site)
      //
      // set the recorder & determiner to those of the observation, or set them null
      // val recorder = if (metadata.recorderAndDeterminerArePublic) o.getRecorderID else null
      // val determiner = if (metadata.recorderAndDeterminerArePublic) o.getDeterminerID else null
      // p.setRecorderID(recorder)
      // p.setDeterminerID(determiner)
      //
      // set the feature to a potentially parent grid square feature
      // p.setFeatureID(feature)
    }

    repo.getTaxonObservationPublic(o.getId) match {
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
          log.info("Not creating public record.")
        // }
      }
    }
  }
}
