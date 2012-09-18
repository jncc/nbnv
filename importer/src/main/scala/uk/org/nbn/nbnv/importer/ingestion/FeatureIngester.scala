package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.ImportFailedException
import uk.org.nbn.nbnv.jpa.nbncore.{Dataset, Feature}
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.spatial.GridSquareFactory

class FeatureIngester @Inject()(repo: Repository, gridSquareFactory: GridSquareFactory) {
  def getFeature(record: NbnRecord) : Feature = {


    if(record.gridReference != null && record.gridReference.isEmpty == false) {
      getFeatureByGridRef(record.gridReference, record.gridReferenceType, record.gridReferencePrecision)
    }
    else if (record.featureKey != null && record.featureKey.isEmpty == false ) {
      getFeatureByFeatureKey(record.featureKey)
    }
    // No need to check the other coordiante elements - the validator will have done this.
    else if (record.east != null && record.east.isEmpty == false) {
      //todo: wire this up to getFeatureByCoord
      new Feature()
    }
    else {
      throw new ImportFailedException("No feature specified.")
    }
  }


  private def getFeatureByGridRef(gridRef: String, gridReferenceType: String = "", gridReferencePrecision: Int = 0) = {

    var gridSquare = gridSquareFactory.getGridSquare(gridRef, gridReferenceType, gridReferencePrecision)

    var feature = repo.getFeatureByGridRef(gridSquare.gridReference)

    if (feature == null) {
      //todo: use the data from gridSquare to create feature.
      feature = new Feature()
    }

    feature
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
