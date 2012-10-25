package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.ImportFailedException
import uk.org.nbn.nbnv.jpa.nbncore.{SiteBoundaryDataset, GridSquare, Feature}
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import uk.org.nbn.nbnv.importer.spatial.{GridSquareInfo, GridSquareInfoFactory}
import javax.persistence.EntityManager
import org.apache.log4j.Logger

class FeatureIngester @Inject()(log: Logger, db: Database, gridSquareInfoFactory: GridSquareInfoFactory) {

  def ensureFeature(record: NbnRecord) : Feature = {

    if (record.gridReference.isDefined) {
      ensureGridRefFeature(record.gridReference.get, record.gridReferenceType.get, record.gridReferencePrecision)
    }
    else if (record.featureKey.isDefined) {
      ensureSiteBoundaryFeature(record.featureKey.get)
    }
    else if (record.east.isDefined && record.north.isDefined && record.srs.isDefined) {
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
    db.repo.getGridSquareFeature(info.gridReference) getOrElse {

      log.debug("Creating grid ref '%s'.".format(info.gridReference))

      // the feature doesn't exist, so we need to create it
      val f = db.repo.createFeature(info.wgs84Polygon)
      val projection = db.repo.getProjection(info.projection)
      val resolution = db.repo.getResolution(info.gridReferencePrecision)
      val gs = db.repo.createGridRef(f, info.gridReference, resolution, projection, info.sourceProjectionPolygon)

      // if square should have a parent, ensure that it does, and set it
      info.getParentGridSquareInfo match {
        case Some(parentInfo) => {
          val (_, parentSquare) = ensureGridSquareFeatureRecursive(parentInfo)
          gs.setParentGridSquare(parentSquare)
        }
        case None => ()
      }
      log.debug("Persisting gridsquare '%s'...".format(info.gridReference))
      db.em.persist(gs)
      (f, gs)
    }
  }

  def ensureSiteBoundaryFeature(featureKey : String) = {

    // feature key - first 8 chars are the siteBoundaryDataset key; remaining are the provider key
    val siteBoundaryDatasetKey = featureKey.substring(0, 8)
    val providerKey = featureKey.substring(8)

    // throws if the (Feature, SiteBoundary) pair doesn't exist
    db.repo.getSiteBoundaryFeature(siteBoundaryDatasetKey, providerKey)._1
  }

  private def ensureGridRefFeatureByCoordinate(easting: Double, northing: Double, spatialReferenceSystem: Int, gridReferencePrecision: Int = 0) = {

    val info = gridSquareInfoFactory.getByCoordinate(easting, northing, spatialReferenceSystem, gridReferencePrecision)

    info match {
      case Some(i) => ensureGridSquareFeatureRecursive(i)._1
      case None => ensureWgs84PointFeature(easting, northing) // no corresponding square; just save the point
    }

  }

  private def ensureWgs84PointFeature(latitude: Double, longitude: Double) =  {
    val wkt = "POINT(%s %s)".format(longitude, latitude)
    db.repo.createFeature(wkt)
  }
}

