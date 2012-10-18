package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.ImportFailedException
import org.geotools.geometry.GeneralDirectPosition
import org.geotools.referencing.ReferencingFactoryFinder
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory

trait GridSqareInfoCompanion {

  protected def getEpsgCode : Int
  protected def getGridSquareByEastingNorthing(east: Int, north: Int, precision: Int) : GridSquareInfo
  protected def create(gridRef: String, precision: Int = 0) : GridSquareInfo

  def apply(east: Int, north: Int) : GridSquareInfo = {
    this(east, north,0)
  }

  def apply(east: Int, north: Int, precision: Int) : GridSquareInfo = {

    getGridSquareByEastingNorthing(east, north, precision)
  }

  def apply(gridRef : String) : GridSquareInfo = {
    create(gridRef)
  }

  def apply(gridRef : String, precision : Int) : GridSquareInfo = {
    create(gridRef, precision)
  }

  def apply(latitude : Double, longitude: Double) : GridSquareInfo = {
    this(latitude, longitude, 0)
  }

  def apply(latitude : Double, longitude: Double, precision : Int) : GridSquareInfo = {
    val (easting, northing) = reprojectToWgs84(latitude, longitude, getEpsgCode)

    //wrap up invalid easting and northing in more relevant error
    try {
      this(easting, northing, precision)
    }
    catch {
      case ife: ImportFailedException =>
        throw new ImportFailedException("Lat '%s' 'Lng are not in the '%s' projection".format(latitude, longitude, getEpsgCode))
      case e: Throwable => throw e
    }
  }


  private def reprojectToWgs84(lat: Double, lng: Double, targetEpsgCode : Int) : (Int, Int) = {
    val latLngGdp = new GeneralDirectPosition(lat, lng)

    //Get the Source CRS to WGS84 transformation operation
    val crsFac = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG",null)
    val wgs84crs = crsFac.createCoordinateReferenceSystem("4326")
    val targetCrs = crsFac.createCoordinateReferenceSystem(targetEpsgCode.toString)
    val transformer = new DefaultCoordinateOperationFactory().createOperation(wgs84crs, targetCrs).getMathTransform

    //Get the coordinates in WGS84 lat lng
    val eastingNorthing = transformer.transform(latLngGdp, latLngGdp).getCoordinates

    //floor the value to find the closest bottom left grid corner
    (eastingNorthing(0).floor.toInt, eastingNorthing(1).floor.toInt)
  }
}
