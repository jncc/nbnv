package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.BadDataException
import org.geotools.geometry.GeneralDirectPosition
import org.geotools.referencing.ReferencingFactoryFinder
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory

trait GridSqareInfoCompanion {

  protected def getEpsgCode : Int
  protected def getGridSquareByEastingNorthing(east: Int, north: Int, precision: Option[Int]) : GridSquareInfo
  protected def create(gridRef: String, precision: Option[Int]) : GridSquareInfo

  def apply(east: Int, north: Int) : GridSquareInfo = {
    this(east, north,None)
  }

  def apply(east: Int, north: Int, precision: Option[Int]) : GridSquareInfo = {

    getGridSquareByEastingNorthing(east, north, precision)
  }

  def apply(gridRef : String) : GridSquareInfo = {
    create(gridRef, None)
  }

  def apply(gridRef : String, precision : Option[Int]) : GridSquareInfo = {
    create(gridRef, precision)
  }

  def apply(latitude : Double, longitude: Double) : GridSquareInfo = {
    this(latitude, longitude, None)
  }

  def apply(latitude : Double, longitude: Double, precision : Option[Int]) : GridSquareInfo = {
    val (easting, northing) = reprojectToWgs84(latitude, longitude, getEpsgCode)

    //wrap up invalid easting and northing in more relevant error
    try {
      this(easting, northing, precision)
    }
    catch {
      case ife: BadDataException =>
        throw new BadDataException("Lat '%s' 'Lng are not in the '%s' projection".format(latitude, longitude, getEpsgCode))
      case e: Throwable => throw e
    }
  }

  //Returns true if easting / northing can be converted to a grid square and false + error meassage if it can't
  def testEastingNorthing(east: Int, north: Int) : (Boolean, Option[String]) = {
    try {
      getGridSquareByEastingNorthing(east, north, None)
      (true, None)
    }
    catch {
      case ife: BadDataException => (false, Some(ife.message))
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
