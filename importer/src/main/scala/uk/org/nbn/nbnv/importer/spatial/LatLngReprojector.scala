package uk.org.nbn.nbnv.importer.spatial

import org.geotools.geometry.GeneralDirectPosition
import org.geotools.referencing.ReferencingFactoryFinder
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory

class LatLngReprojector {
  def reproject(lat: Double, lng: Double, targetSRS : String) : (Int, Int) = {
    val latLngGdp = new GeneralDirectPosition(lat, lng)

    //Get the Source CRS to WGS84 transformation operation
    val crsFac = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG",null)
    val wgs84crs = crsFac.createCoordinateReferenceSystem("4326")
    val targetCrs = crsFac.createCoordinateReferenceSystem(targetSRS)
    val transformer = new DefaultCoordinateOperationFactory().createOperation(wgs84crs, targetCrs).getMathTransform

    //Get the coordinates in WGS84 lat lng
    val eastingNorthing = transformer.transform(latLngGdp, latLngGdp).getCoordinates

    (eastingNorthing(0).floor.toInt, eastingNorthing(1).floor.toInt)
  }
}
