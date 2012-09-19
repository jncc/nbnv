package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.ImportFailedException
import uk.org.nbn.nbnv.jpa.nbncore.Feature
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.spatial.{GridSquareInfo, GridSquareInfoFactory}
import javax.persistence.EntityManager

class FeatureIngester @Inject()(em: EntityManager, repo: Repository, gridSquareInfoFactory: GridSquareInfoFactory) {

  def ensureFeature(record: NbnRecord) : Feature = {

    if (record.gridReference.isDefined) {
        ensureGridRefFeature(record.gridReference.get, record.gridReferenceType.get, record.gridReferencePrecision)
    }
    else if (record.featureKey.isDefined) {
        getFeatureByFeatureKey(record.featureKey.get)
    }
    else if (record.east.isDefined) {
      // no need to check the other coordinate elements - the validator will have done this
      // todo: wire this up to getFeatureByCoord
      new Feature()
    }
    else {
      throw new ImportFailedException("No feature specified.")
    }
  }

  private def ensureGridRefFeature(gridRef: String, gridReferenceType: String, gridReferencePrecision: Int) = {

    // ensures that the Feature corresponding to the GridSquareInfo, and all its parents, exist
    def ensure(info: GridSquareInfo) : (Feature, uk.org.nbn.nbnv.jpa.nbncore.GridSquare) = {

      // if there's a feature already, all necessary parents should already exist, so just return it
      repo.getGridSquareFeature(info.gridReference).getOrElse {

        // create, persist, and recurse for all necessary parents

        // the feature doesn't exist, so we need to create it
        val f = new Feature
        f.setWkt(info.wgs84Polygon)
        // call a procedure to generate geom from wkt - see usp_SpatialLocation_AddLocation in the bars db. don't worry about the STIsValid stuff; it will always be valid because were generating the WKT ourselfes
        // the second argument from STGeomFromText is the spatial reference id. bars uses osgb 277000 NBN uses WSG84.
        // ...
        em.persist(f)
        val s = new uk.org.nbn.nbnv.jpa.nbncore.GridSquare
        s.setFeatureID(f)
        s.setGridRef(gridRef)
//        s.setProjectionID()
//        s.setResolutionID()

        // don't need to do anything if no parent, because we're at the topmost
        info.getParentGridRef map { parentInfo =>
          val (_, parentSquare) = ensure(parentInfo)
          s.setParentSquare(parentSquare)
        }
        em.persist(s)
        (f, s)
      }
    }

    val info = gridSquareInfoFactory.getGridSquare(gridRef, gridReferenceType, gridReferencePrecision)
    ensure(info)._1
  }

  private def getFeatureByFeatureKey(featureKey : String) = {
    //todo: Get feature by id
    new Feature()
    //split feature key - first 8 char = dataset key , remainder = SiteBoundary.providerKey
    //Retreive feature by dataset and provider key from site boundary
    //throw exception if not retreived.
  }

  private def getFeatureByCoordinate(easting: Int, northing: Int, spatailReferenceSystem: Int, gridReferencePrecision: Int = 0) = {
     //Get nearest grid square at 100m or at the grid reference resolution specified.

    new Feature()
  }
}
