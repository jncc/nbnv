package uk.org.nbn.nbnv.importer.spatial

import math._

class ChannelIslandGridSquareInfo(gridRef: String, precision: Int = 0) extends GridSquareInfo(gridRef, precision) {

  def projection =  "ED50"

  def epsgCode = "23030"

  protected def getEastingNorthing(gridRef: String) = {
    //Bottom Left corner of the grid squares
    val WAbl = (500000,5500000)
    val WVbl = (500000,5400000)

    val g = getTenFigGridRef(gridRef)
    val numerals = getNumeralsFromGridRef(g).splitAt(5)

    getLettersFromGridRef(g) match {
      case "WA" => (WAbl._1 + numerals._1.toInt, WAbl._2 + numerals._2.toInt)
      case "WV" => (WVbl._1 + numerals._1.toInt, WVbl._2 + numerals._2.toInt)
    }
  }

  protected def create(gridRef: String, precision: Int = 0) = {
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
