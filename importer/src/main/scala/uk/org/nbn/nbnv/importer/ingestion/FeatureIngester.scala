package uk.org.nbn.nbnv.importer.ingestion

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.ImportFailedException
import uk.org.nbn.nbnv.jpa.nbncore.Feature
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.data.Repository

class FeatureIngester @Inject()(repo: Repository) {
  def getFeature(record: NbnRecord) : Feature = {


    if(record.gridReference != null && record.gridReference.isEmpty == false) {
      getFeatureByGridRef(record.gridReference, record.gridReferenceType, record.gridReferencePrecision)
    }
    else if (record.featureKey != null && record.featureKey.isEmpty == false ) {
      new Feature()
    }
    // No need to check the other coordiante elements - the validator will have done this.
    else if (record.east != null && record.east.isEmpty == false) {
      new Feature()
    }
    else {
      throw new ImportFailedException("No feature specified.")
    }
  }

  private def getFeatureByGridRef(gridRef: String, gridReferenceType: String = "", gridReferencePrecision: Int = 0) = {
    //todo: Use precision to blur the grid ref to the required resolution
    var feature = repo.getFeatureByGridRef(gridRef)

    if (feature == null) {
      //Get coordinates from grid ref
      //Reproject in WGS84
      //Generate Polygon and save as feature
      //Repeat for all parent grid squares
    }

    feature
  }

  private def getFeatureByFeatureKey(featureKey : String) = {
     
  }

  private def getFeatureByCoordinate(easting: Int, northing: Int, spatailReferenceSystem: Int) = {
     //not sure about this one
  }
}
