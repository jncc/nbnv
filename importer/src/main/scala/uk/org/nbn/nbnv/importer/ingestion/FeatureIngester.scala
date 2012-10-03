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
    else if (record.east.isDefined) {
      // no need to check the other coordinate elements - the validator will have done this
      // todo: wire this up to getFeatureByCoord
      new Feature()
    }
    else {
      throw new ImportFailedException("No feature specified.")
    }
  }

  def ensureGridRefFeature(gridRef: String, gridReferenceType: String = "", gridReferencePrecision: Int = 0) = {

    // ensures that the Grid Feature corresponding to the GridSquareInfo, and all its parents, exist
    def ensure(info: GridSquareInfo) : (Feature, GridSquare) = {

      // if there's a feature already, all necessary parents should already exist, so just return it
      repo.getGridSquareFeature(info.gridReference) getOrElse {

        log.debug("Creating grid ref '%s'.".format(info.gridReference))

        // the feature doesn't exist, so we need to create it
        val f = repo.createFeature(info.wgs84Polygon)
        // ... todo
//        f.setGeom(hex2Bytes("0xE610000001040500000025188E8301E6F0BF28D6E355FD944A4073EB1E5CE1DFF0BFAC4168F2FC944A400F37C3A7CCDFF0BF3EF4D3651A954A40AEC794D7ECE5F0BF8D5C50C91A954A4025188E8301E6F0BF28D6E355FD944A4001000000020000000001000000FFFFFFFF0000000003"))
//        em.persist(f)

        val gs = new GridSquare
        gs.setFeatureID(f)
        gs.setGridRef(info.gridReference)
        //gs.setGeom(tempGeom)

        // set the projection
        val p = repo.getProjection(info.projection)
//        gs.setProjectionID(p)

        // set the resolution
        val r = repo.getResolution(info.gridReferencePrecision)
        gs.setResolutionID(r)

        // if square should have a parent, ensure that it does, and set it
        info.getParentGridSquareInfo match {
          case Some(parentInfo) => {
            val (_, parentSquare) = ensure(parentInfo)
            gs.setParentSquareGridRef(parentSquare)
          }
          case None => ()
        }
        log.debug("Persisting gridsquare '%s'...".format(info.gridReference))
        em.persist(gs)
        (f, gs)
      }
    }

    // a GridSquareInfo object can compute all the info we need about a grid square
    val info = gridSquareInfoFactory.getGridSquareByGridRef(gridRef, gridReferenceType, gridReferencePrecision)

    // ensure that the (Feature, GridSquare) pair exists and return the feature
    ensure(info)._1
  }


  def hex2Bytes( hex: String ): Array[Byte] = {
    (for { i <- 0 to hex.length-1 by 2 if i > 0 || !hex.startsWith( "0x" )}
    yield hex.substring( i, i+2 ))
      .map( Integer.parseInt( _, 16 ).toByte ).toArray
  }

  private def ensureSiteBoundaryFeature(featureKey : String) = {

    // feature key - first 8 chars are the siteBoundaryDataset; remaining are the providerKey
    val siteBoundaryDataset = featureKey.substring(0, 8)
    val providerKey = featureKey.substring(8)

    // throws if the (Feature, SiteBoundary) pair doesn't exist
    repo.getSiteBoundaryFeature(siteBoundaryDataset, providerKey)._1
  }

  private def getFeatureByCoordinate(easting: Int, northing: Int, spatailReferenceSystem: Int, gridReferencePrecision: Int = 0) = {
    //Get nearest grid square at 100m or at the grid reference resolution specified.

    // there will be a new method on the factory to get a gridsquare by point
    // then i need to call the gridsquare ensure method to make sure that the square(s) exist
//    need to resolve a point (coord) to nearest gridsquare
//      not expecting points in channel islands
    new Feature()
  }
}

