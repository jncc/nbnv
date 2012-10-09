package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.ImportFailedException
import uk.org.nbn.nbnv.jpa.nbncore.{GridSquare, Feature}
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.spatial.{GridSquareInfo, GridSquareInfoFactory}
import javax.persistence.EntityManager
import org.apache.log4j.Logger

class FeatureIngester @Inject()(log: Logger, em: EntityManager, repo: Repository, gridSquareInfoFactory: GridSquareInfoFactory) {

  def ensureFeature(record: NbnRecord) : Feature = {

    if (record.gridReference.isDefined) {
      ensureGridRefFeature(record.gridReference.get, record.gridReferenceType.get, record.gridReferencePrecision)
    }
    else if (record.featureKey.isDefined) {
      ensureSiteBoundaryFeature(record.featureKey.get)
    }
    else if (record.east.isDefined && record.north.isDefined && record.srs.isDefined) {
      // todo: wire this up to getFeatureByCoord
      ensureGridRefFeatureByCoordinate(record.east.get, record.north.get, record.srs.get, record.gridReferencePrecision)

    }
    else {
      throw new ImportFailedException("No feature specified.")
    }
  }

  def ensureGridRefFeature(gridRef: String, gridReferenceType: String = "", gridReferencePrecision: Int = 0) = {

    // a GridSquareInfo object can compute all the info we need about a grid square
    val info = gridSquareInfoFactory.getByGridRef(gridRef, gridReferenceType, gridReferencePrecision)

    // ensure that the (Feature, GridSquare) pair exists and return the feature
    ensureGridSquareFeatureRecursive(info)._1
  }

  // ensures that the Grid Feature corresponding to the GridSquareInfo, and all its parents, exist
  private def ensureGridSquareFeatureRecursive(info: GridSquareInfo) : (Feature, GridSquare) = {

    // if there's a feature already, all necessary parents should already exist, so just return it
    repo.getGridSquareFeature(info.gridReference) getOrElse {

      log.debug("Creating grid ref '%s'.".format(info.gridReference))

      // the feature doesn't exist, so we need to create it
      val f = repo.createFeature(info.wgs84Polygon)
      val projection = repo.getProjection(info.projection)
      val resolution = repo.getResolution(info.gridReferencePrecision)
      val gs = repo.createGridRef(f, info.gridReference, resolution, projection, info.sourceProjectionPolygon)

      // if square should have a parent, ensure that it does, and set it
      info.getParentGridSquareInfo match {
        case Some(parentInfo) => {
          val (_, parentSquare) = ensureGridSquareFeatureRecursive(parentInfo)
          gs.setParentGridSquare(parentSquare)
        }
        case None => ()
      }
      log.debug("Persisting gridsquare '%s'...".format(info.gridReference))
      em.persist(gs)
      (f, gs)
    }
  }

  private def ensureSiteBoundaryFeature(featureKey : String) = {

    // feature key - first 8 chars are the siteBoundaryDataset; remaining are the providerKey
    val siteBoundaryDataset = featureKey.substring(0, 8)
    val providerKey = featureKey.substring(8)

    // throws if the (Feature, SiteBoundary) pair doesn't exist
    repo.getSiteBoundaryFeature(siteBoundaryDataset, providerKey)._1
  }

  private def ensureGridRefFeatureByCoordinate(easting: Double, northing: Double, spatialReferenceSystem: String, gridReferencePrecision: Int = 0) = {
    //Get nearest grid square at 100m or at the grid reference resolution specified.

    val info = gridSquareInfoFactory.getByCoordinate(easting, northing, spatialReferenceSystem, gridReferencePrecision)

    ensureGridSquareFeatureRecursive(info)._1

    // there will be a new method on the factory to get a gridsquare by point
    // then i need to call the gridsquare ensure method to make sure that the square(s) exist
//    need to resolve a point (coord) to nearest gridsquare
//      not expecting points in channel islands


    new Feature()
  }
}

