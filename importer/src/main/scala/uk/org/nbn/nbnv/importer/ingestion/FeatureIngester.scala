package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.ImportFailedException
import uk.org.nbn.nbnv.jpa.nbncore.Feature
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.spatial.GridSquareFactory

class FeatureIngester @Inject()(repo: Repository, gridSquareFactory: GridSquareFactory) {

  def ensureFeature(record: NbnRecord) : Feature = {

    if (record.gridReference.isDefined) {
        getFeatureByGridRef(record.gridReference.get, record.gridReferenceType.get, record.gridReferencePrecision)
    }
    else if (record.featureKey.isDefined) {
        getFeatureByFeatureKey(record.featureKey.get)
    }
    else if (record.east.isDefined) {
      // no need to check the other coordinate elements - the validator will have done this
      //todo: wire this up to getFeatureByCoord
      new Feature()
    }
    else {
      throw new ImportFailedException("No feature specified.")
    }
  }


  private def getFeatureByGridRef(gridRef: String, gridReferenceType: String = "", gridReferencePrecision: Int = 0) = {

    // this doesn't go to the db - just calculates stuff i need
    val gridSquare = gridSquareFactory.getGridSquare(gridRef, gridReferenceType, gridReferencePrecision)

    // if there's a feature already, that also means all the parents should also be in there
    // (the importer will always create all the parents)
    repo.getGridSquareFeature(gridSquare.gridReference) match {
      case Some(feature) => {
        feature
      }
      case None => {
        // if it doesn't exist, create it and then, recursively, its parents

        new Feature
      }
    }
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
