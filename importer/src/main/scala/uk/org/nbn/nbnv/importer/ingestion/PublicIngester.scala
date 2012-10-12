package uk.org.nbn.nbnv.importer.ingestion

import com.google.inject.Inject
import org.apache.log4j.Logger
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import uk.org.nbn.nbnv.importer.spatial.GridSquareInfoFactory
import uk.org.nbn.nbnv.metadata.Metadata
import uk.org.nbn.nbnv.jpa.nbncore.{TaxonObservation, Sample, TaxonObservationPublic}

/// Creates, updates or deletes the TaxonObservationPublic record appropriately.

class PublicIngester @Inject()(log: Logger,
                               db: Database,
                               gridSquareInfoFactory: GridSquareInfoFactory,
                               featureIngester: FeatureIngester) {

  def ingestPublic(o: TaxonObservation, sample: Sample, metadata: Metadata) {

    def update(p: TaxonObservationPublic) {


      // set the fields which are always the same in the public record
      //p.setObservationID(o.getObservationID)
      //p.setSampleID(o.getSampleID)
      //p.setObservationKey(o.getObservationKey)
      //p.setTaxonObservationID(o.getId)

      // set the site - to null if necessary
      //val publicSite = if (metadata.siteIsPublic) o.getSiteID else null
      //p.setSiteID(publicSite.getId)

      // set the recorder & determiner - to null if necessary
      //val publicRecorder = if (metadata.recorderAndDeterminerArePublic) o.getRecorderID else null
      //val publicDeterminer = if (metadata.recorderAndDeterminerArePublic) o.getDeterminerID else null
      //p.setRecorderID(publicRecorder)
      //p.setDeterminerID(publicDeterminer)

      // set the feature - blurring to a potentially lower-precision feature if necessary
      // obviously we only blur gridsquare features (including points which have been represented as such)
      //repo.getGridSquareFeature(o.getFeatureID.getFeatureID) match {
      //  case Some((_, gridSquare)) => {
      //    val info = gridSquareInfoFactory.getGridSquare(gridSquare.getGridRef)
      //    val publicInfo = info.getLowerPrecisionGridSquareInfo(metadata.publicPrecision)
      //    val publicFeature = featureIngester.ensureGridRefFeature(publicInfo.gridReference, publicInfo.projection, publicInfo.gridReferencePrecision)
      //    p.setFeatureID(publicFeature)
      //  }
      //  case None => {
      //    // the feature was not a gridsquare feature
      //    p.setFeatureID(o.getFeatureID)
      //  }
      //}
    }

    db.repo.getTaxonObservationPublic(o.getId) match {
      case Some(p) => {
        // delete the public record if it's become sensitive
        if (o.getSensitiveRecord) {
          log.info("Deleting public record...")
          db.em.remove(p)
        } else {
          log.info("Updating public record...")
          update(p)
        }
      }
      case None => {
        // don't create the public record if it is sensitive
        if (o.getSensitiveRecord) {
          log.info("Not creating public record (sensitive=true).")
        } else {
          log.info("Creating public record...")
          val p = new TaxonObservationPublic
          update(p)
          db.em.persist(p)
        }
      }
    }
  }
}
