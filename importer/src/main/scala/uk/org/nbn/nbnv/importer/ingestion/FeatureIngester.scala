package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.importer.records._
import uk.org.nbn.nbnv.importer.BadDataException
import uk.org.nbn.nbnv.jpa.nbncore._
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import uk.org.nbn.nbnv.importer.spatial.{GridSquareInfo, GridSquareInfoFactory}
import javax.persistence.EntityManager
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.records.GridRefDef
import scala.Some
import uk.org.nbn.nbnv.importer.records.PointDef
import uk.org.nbn.nbnv.importer.records.GridRefDef
import uk.org.nbn.nbnv.importer.records.BoundaryDef
import scala.Some
import uk.org.nbn.nbnv.importer.records.PointDef

class FeatureIngester @Inject()(log: Logger, db: Database, gridSquareInfoFactory: GridSquareInfoFactory) {
  
  //get feature
  //ensure grid features
  def getBoundaryFeatureId(boundary : BoundaryDef) : Int = {
    getSiteBoundaryFeature(boundary).getId
  }
  
  def getGridFeature(record: NbnRecord) : ImportFeature = record.feature match {
    case value: GridRefDef  => {
      val info = gridSquareInfoFactory.getByGridRef(value)
      db.repo.getGridSquareFeature(info.gridReference).get._1
    }
    case value: PointDef    => ensurePointFeatureByCoordinate(value)
  }

  def ensureGridSquareFeature(record: NbnRecord) {
    record.feature match {
      case value: GridRefDef  => ensureGridRefFeature(value)
      case value: PointDef    => ensureGridRefFeatureByCoordinate(value)
      case _ => //nothing needs to be done for boundary defs
    }
   }

  def ensureGridRefFeature(value: GridRefDef) = {

    // a GridSquareInfo object can compute all the info we need about a grid square
    val info = gridSquareInfoFactory.getByGridRef(value)

    // ensure that the (Feature, GridSquare) pair exists and return the feature
    ensureGridSquareFeatureRecursive(info)._1
  }

  // ensures that the Grid Feature corresponding to the GridSquareInfo, and all its parents, exist
  private def ensureGridSquareFeatureRecursive(info: GridSquareInfo) : (ImportFeature, ImportGridSquare) = {

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
          gs.setParentSquareGridRef(parentSquare)
        }
        case None => ()
      }
      log.debug("Persisting gridsquare '%s'...".format(info.gridReference))
      db.em.persist(gs)
      (f, gs)
    }
  }

  def getSiteBoundaryFeature(value: BoundaryDef) = {

    // feature key - first 8 chars are the siteBoundaryDataset key; remaining are the provider key
    val siteBoundaryDatasetKey = value.key.substring(0, 8)
    val providerKey = value.key.substring(8)

    // throws if the (Feature, SiteBoundary) pair doesn't exist
    db.repo.getSiteBoundaryFeature(siteBoundaryDatasetKey, providerKey)._1
  }

  private def ensureGridRefFeatureByCoordinate(value: PointDef) = {

   val info = gridSquareInfoFactory.getByCoordinate(value)

   info match {
     case Some(i) =>  ensureGridSquareFeatureRecursive(i)._1
     case None =>
   }
  }

  private def ensurePointFeatureByCoordinate(value: PointDef) = {
    val info = gridSquareInfoFactory.getByCoordinate(value)
    
    info match {
      case Some(i) =>  db.repo.getGridSquareFeature(i.gridReference).get._1
      case None =>  ensureWgs84PointFeature(value.east, value.north)
    }
  }
  
  private def ensureWgs84PointFeature(latitude: Double, longitude: Double) =  {
    val wkt = "POINT(%s %s)".format(longitude, latitude)
    db.repo.createFeature(wkt)
  }
}

