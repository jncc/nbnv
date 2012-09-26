package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.ImportFailedException
import org.geotools.geometry.GeneralDirectPosition
import org.geotools.referencing.ReferencingFactoryFinder
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory

trait GridSquareInfo {
  def projection : String
  def gridReference : String
  def gridReferencePrecision : Int
  def wgs84Polygon : String
  def sourcePolygon : String
  def getParentGridRef : Option[GridSquareInfo]
  def getLowerPrecisionGridRef(precision: Int) : GridSquareInfo

  protected val dintyGridByCoord = Map (
    (0,8) -> "E", (2,8) -> "J", (4,8) -> "P", (6,8) -> "U", (8,8) -> "Z",
    (0,6) -> "D", (2,6) -> "I", (4,6) -> "N", (6,6) -> "T", (8,6) -> "Y",
    (0,4) -> "C", (2,4) -> "H", (4,4) -> "M", (6,4) -> "S", (8,4) -> "X",
    (0,2) -> "B", (2,2) -> "G", (4,2) -> "L", (6,2) -> "R", (8,2) -> "W",
    (0,0) -> "A", (2,0) -> "F", (4,0) -> "K", (6,0) -> "Q", (8,0) -> "V"
  )

  protected val dintyGridByLetter = dintyGridByCoord map {_.swap}

  protected def getDintyLeter(easting: Int, northing: Int) = {
    val dintyEasting = easting - (easting % 2)
    val dintyNorthing = northing - (northing % 2)

    dintyGridByCoord(dintyEasting, dintyNorthing)
  }


  protected def getNormalisedPrecision(precision : Int) = {
    if (precision <= 100) {
      100
    }
    else if (precision <= 1000) {
      1000
    }
    else if (precision <= 2000) {
      2000
    }
    else if (precision <= 10000) {
      10000
    }
    else {
      throw new ImportFailedException("Bad precision entry > 10000 : %s".format(precision))
    }
  }

  protected def getWGS84PolygonFromGridPoint(easting: Int, northing: Int, gridSize: Int, epsgCode: String) = {
    val blGdp = new GeneralDirectPosition(easting, northing)
    val brGdp = new GeneralDirectPosition(easting + gridSize, northing)
    val tlGdp = new GeneralDirectPosition(easting, northing + gridSize)
    val trGdp = new GeneralDirectPosition(easting + gridSize, northing + gridSize)

    //Get the Source CRS to WGS84 transformation operation
    val crsFac = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG",null)
    val wgs84crs = crsFac.createCoordinateReferenceSystem("4326")
    val SourceCrs = crsFac.createCoordinateReferenceSystem(epsgCode)
    val transformer = new DefaultCoordinateOperationFactory().createOperation(SourceCrs, wgs84crs).getMathTransform

    //Get the coordinates in WGS84 lat lng
    val bl = transformer.transform(blGdp, blGdp).getCoordinates
    val br = transformer.transform(brGdp, brGdp).getCoordinates
    val tl = transformer.transform(tlGdp, tlGdp).getCoordinates
    val tr = transformer.transform(trGdp, trGdp).getCoordinates

    "POLYGON((" + bl(0) + " " + bl(1) + ", " +
      tl(0) + " " + tl(1) + ", " +
      tr(0) + " " + tr(1) + ", " +
      br(0) + " " + br(1) + ", " +
      bl(0) + " " + bl(1) + "))"
  }



}
