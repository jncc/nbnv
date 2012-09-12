package uk.org.nbn.nbnv.importer.spatial

import uk.me.jstott.jcoord.OSRef

class BritishGridSquare(gridRef : String, precision: Int = 0) extends GridSquare {

  val roundedPrecision = {
    if (precision > 10000) {
      10000
    }
    else if (10000 > precision > )
  }



  val outputGridRef = {
    //Get Current precision of gridref
    //If current precision >
    //else If precision > current precision
    //  throw invalid arugument exception - invalid precision
    //else If precision < current precision
    //  decrease gridref to precision
    //else If currentPrecision > 100
    //  decrease gridref to precision of 100

  }

  def gridReference = outputGridRef

  def gridReferencePrecision = 0

  def wgs84Polygon = null

  def getParentGridRef = getPrecision(outputGridRef)

  //Returns the grid reference precision in meters
  private def getPrecision(gridReference : String) = {
    if (gridRef.matches("""^[HNOST][A-Z]$""")) {
      500000
    }
    else if (gridRef.matches("""(?i)^[HNOST][A-Z]\d{2}[A-Z]$""")) {
      2000
    }
    else {
      //Otherwise the precisoin is inversly proportional to the number of digits
      var numerals = gridRef.substring(2, gridRef.length - 1).length
      1000000 / 10 ^ (numerals / 2)
    }
  }
}
