package uk.org.nbn.nbnv.importer.ingestion

import com.google.inject.Inject
import org.apache.log4j.Logger
import javax.persistence.EntityManager
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import uk.org.nbn.nbnv.importer.spatial.GridSquareInfoFactory
import uk.org.nbn.nbnv.importer.metadata.Metadata
import uk.org.nbn.nbnv.jpa.nbncore._
import uk.org.nbn.nbnv.importer.records.{GridTypeDef, GridRefDef}
import uk.org.nbn.nbnv.importer.records.GridRefDef
import uk.org.nbn.nbnv.importer.records.GridTypeDef
import scala.Some

/// Creates, updates or deletes the TaxonObservationPublic record appropriately.

class PublicIngester @Inject()(log: Logger,
                               db: Database,
                               gridSquareInfoFactory: GridSquareInfoFactory,
                               featureIngester: FeatureIngester) {

  def ingestPublic(o: ImportTaxonObservation, sample: ImportSample, metadata: Metadata) {

    log.debug("Ingesting public record...")

    def update(p: ImportTaxonObservationPublic) {

      // associate with the main observation record
      p.setImportTaxonObservation(o)
      p.setTaxonObservationID(o.getId)

      // set the recorder & determiner - to null if necessary
      val publicRecorder = if (metadata.recorderAndDeterminerArePublic) o.getRecorderID else null
      val publicDeterminer = if (metadata.recorderAndDeterminerArePublic) o.getDeterminerID else null
      p.setRecorderID(publicRecorder)
      p.setDeterminerID(publicDeterminer)

      // set the feature - blurring to a potentially lower-precision feature if necessary
      // obviously we only blur gridsquare features (including points which have been represented as such)
      db.repo.getGridSquareFeature(o.getFeatureID) match {
        case Some((_, gridSquare)) => {
          val info = gridSquareInfoFactory.getByGridRef(GridRefDef(gridSquare.getGridRef, None, None))
          val publicInfo = info.getLowerPrecisionGridSquareInfo(metadata.publicPrecision)
          val publicFeature = featureIngester.ensureGridRefFeature(GridRefDef(publicInfo.gridReference, Some(GridTypeDef(publicInfo.projection)), Some(publicInfo.gridReferencePrecision)))
          p.setFeatureID(publicFeature.getId)

          val publicSiteId =  if (info.gridReferencePrecision == publicInfo.gridReferencePrecision) o.getSiteID else null
          p.setSiteID(publicSiteId)
        }
        case None => {
          // the feature was not a gridsquare feature
          p.setFeatureID(o.getFeatureID)

          p.setSiteID(o.getSiteID)
        }
      }

    }

    if (o.getSensitiveRecord) {
      log.debug("Not creating public record (sensitive=true).")
    } else {
      log.debug("Creating public record...")
      val p = new ImportTaxonObservationPublic
      update(p)
      db.em.persist(p)
    }
  }
}
