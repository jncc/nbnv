package uk.org.nbn.nbnv.importer.spatial

import math._
import uk.org.nbn.nbnv.importer.BadDataException

object ChannelIslandGridSquareInfo extends GridSqareInfoCompanion {

  protected def create(gridRef: String, precision: Option[Int]) = {
    new ChannelIslandGridSquareInfo(gridRef, precision)
  }

  protected def getEpsgCode = 23030

  protected def getGridSquareByEastingNorthing(east: Int, north: Int, precision: Option[Int]) ={
    val eastStr = east.toString
    val northStr = north.toString

    val gridLetters =
      northStr.substring(0,2) match {
        case "55" => "WA"
        case "54" => "WV"
        case _ => throw throw new BadDataException("The easing and northing (%s,%s) are not within the Channel Islands grid".format(east,north))
      }

    val eastPart = eastStr.substring(1,eastStr.length - 1)
    val northPart = northStr.substring(2, northStr.length - 1)

    val gridRef = gridLetters + eastPart + northPart

    create(gridRef, precision)
  }
}

class ChannelIslandGridSquareInfo(gridRef: String, precision: Option[Int]) extends GridSquareInfo(gridRef, precision) {

  def projection =  "ED50"

  def epsgCode = ChannelIslandGridSquareInfo.getEpsgCode

  def getEastingNorthing = {
    //Bottom Left corner of the grid squares
    val WAbl = (500000,5500000)
    val WVbl = (500000,5400000)

    val g = getTenFigGridRef(outputGridRef)
    val numerals = getNumeralsFromGridRef(g).splitAt(5)

    getLettersFromGridRef(g) match {
      case "WA" => (WAbl._1 + numerals._1.toInt, WAbl._2 + numerals._2.toInt)
      case "WV" => (WVbl._1 + numerals._1.toInt, WVbl._2 + numerals._2.toInt)
    }
  }

  protected def create(gridRef: String, precision: Option[Int]) = {
    new ChannelIslandGridSquareInfo(gridRef, precision)
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
