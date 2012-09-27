package uk.org.nbn.nbnv.importer.spatial

import math._
import uk.org.nbn.nbnv.importer.ImportFailedException

class ChannelIslandGridSquareInfo(gridRef: String, precision: Int = 0) extends GridSquareInfo(gridRef, precision) {

  def projection =  "ED50"

  def getLowerPrecisionGridSquareInfo(precision: Int) = new ChannelIslandGridSquareInfo(outputGridRef, precision)

  def sourceProjectionPolygon = {
    val gridSize = gridReferencePrecision

    val (easting, northing) = getEastingNorthing(outputGridRef)

    getPolygonFromGridSquareOrigin(easting, northing, gridSize)
  }

  def wgs84Polygon = {

    val gridSize = gridReferencePrecision

    val (easting, northing) = getEastingNorthing(outputGridRef)

    getWGS84PolygonFromGridSquareOrigin(easting, northing, gridSize, "23030")
  }

  //WV 59500  47500
  //E = 559500
  //N = 5447500
  private def getEastingNorthing(gridRef: String) = {
    //Bottom Left corner of the grid squares
    val WAbl= (500000,5500000)
    val WVbl = (500000,5400000)

    val g = getTenFigGridRef(gridRef)
    val numerals = getNumeralsFromGridRef(g).splitAt(5)

    getLettersFromGridRef(g) match {
      case "WA" => (WAbl._1 + numerals._1.toInt, WAbl._2 + numerals._2.toInt)
      case "WV" => (WVbl._1 + numerals._1.toInt, WVbl._2 + numerals._2.toInt)
    }
  }

  def getParentGridSquareInfo: Option[ChannelIslandGridSquareInfo] = {
    if (gridReferencePrecision == 10000) {
      None
    }
    else {
      //get parent grid reference
      val parentGridReference =
        if (gridReferencePrecision == 100) {
          decreaseGridPrecision(outputGridRef, 1000)
        }
        else if (gridReferencePrecision == 1000) {
          decreaseGridPrecision(outputGridRef, 2000)
        }
        else if (gridReferencePrecision == 2000) {
          decreaseGridPrecision(outputGridRef, 10000 )
        }
        else {
          throw new RuntimeException("Current grid reference has an invalid precision")
        }

      Option(new ChannelIslandGridSquareInfo(parentGridReference))
    }
  }

  //Check grid ref is uk grid ref
  protected def checkGridRef {
    if (gridRef.matches(GridRefPatterns.channelIslandsGridRef) == false
      && gridRef.matches(GridRefPatterns.channelIslandsDintyGridRef)  == false)
      throw new IllegalArgumentException("Grid reference '%s' is not a valid Channel Islands grid reference".format(gridRef))
  }

  //Returns the grid reference precision in meters
  protected def getPrecision(gridReference : String) = {
    if (gridReference.matches("""^W$""")) {
      500000
    }
    else if (gridReference.matches("""^W[AV]$""")) {
      100000
    }
    else if (gridReference.matches(getDintyRegex)) {
      2000
    }
    else {
      //Otherwise the precision is inversely proportional to ten to the power of the number of digits
      val numerals = getNumeralsFromGridRef(gridReference).length
      (100000 / pow(10, (numerals / 2))).toInt
    }
  }

  protected def getLettersFromGridRef(gridRef : String) = {
    gridRef.substring(0,2)
  }

  protected def getNumeralsFromGridRef(gridRef : String) = {
    gridRef.substring(2, gridRef.length)
  }

  protected def getDintyRegex = GridRefPatterns.channelIslandsDintyGridRef
}
