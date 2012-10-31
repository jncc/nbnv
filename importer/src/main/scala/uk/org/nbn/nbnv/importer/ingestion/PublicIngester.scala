package uk.org.nbn.nbnv.importer.ingestion

import com.google.inject.Inject
import org.apache.log4j.Logger
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import uk.org.nbn.nbnv.importer.spatial.GridSquareInfoFactory
import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.jpa.nbncore.{TaxonObservation, Sample, TaxonObservationPublic}

/// Creates, updates or deletes the TaxonObservationPublic record appropriately.

class PublicIngester @Inject()(log: Logger,
                               db: Database,
                               gridSquareInfoFactory: GridSquareInfoFactory,
                               featureIngester: FeatureIngester) {

  def ingestPublic(o: TaxonObservation, sample: Sample, metadata: Metadata) {

    log.debug("Ingesting public record...")

    def update(p: TaxonObservationPublic) {

      // associate with the main observation record
      p.setTaxonObservation(o)
      p.setTaxonObservationID(o.getId)

      // set the site - to null if necessary
      val publicSite = if (metadata.siteIsPublic) o.getSite else null
      p.setSite(publicSite)

      // set the recorder & determiner - to null if necessary
      val publicRecorder = if (metadata.recorderAndDeterminerArePublic) o.getRecorder else null
      val publicDeterminer = if (metadata.recorderAndDeterminerArePublic) o.getDeterminer else null
      p.setRecorder(publicRecorder)
      p.setDeterminer(publicDeterminer)

      // set the feature - blurring to a potentially lower-precision feature if necessary
      // obviously we only blur gridsquare features (including points which have been represented as such)
      db.repo.getGridSquareFeature(o.getFeature.getId) match {
        case Some((_, gridSquare)) => {
          val info = gridSquareInfoFactory.getByGridRef(gridSquare.getGridRef)
          val publicInfo = info.getLowerPrecisionGridSquareInfo(metadata.publicResolution)
          val publicFeature = featureIngester.ensureGridRefFeature(publicInfo.gridReference, publicInfo.projection, publicInfo.gridReferencePrecision)
          p.setFeature(publicFeature)
        }
        case None => {
          // the feature was not a gridsquare feature
          p.setFeature(o.getFeature)
        }
      }
    }

    db.repo.getTaxonObservationPublic(o.getId) match {
      case Some(p) => {
        // delete the public record if it's become sensitive
        if (o.getSensitiveRecord) {
          log.debug("Deleting public record...")
          db.em.remove(p)
        } else {
          log.debug("Updating public record...")
          update(p)
        }
      }
      case None => {
        // don't create the public record if it is sensitive
        if (o.getSensitiveRecord) {
          log.debug("Not creating public record (sensitive=true).")
        } else {
          log.debug("Creating public record...")
          val p = new TaxonObservationPublic
          update(p)
          db.em.persist(p)
        }
      }
    }
  }
}
