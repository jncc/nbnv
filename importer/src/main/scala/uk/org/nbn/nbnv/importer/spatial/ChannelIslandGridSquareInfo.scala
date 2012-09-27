package uk.org.nbn.nbnv.importer.spatial

import math._
import uk.org.nbn.nbnv.importer.ImportFailedException

class ChannelIslandGridSquareInfo(gridRef: String, precision: Int = 0) extends GridSquareInfo {

  //Check grid ref is uk grid ref
  if (gridRef.matches(GridRefPatterns.channelIslandsGridRef) == false
    && gridRef.matches(GridRefPatterns.channelIslandsDintyGridRef)  == false)
    throw new IllegalArgumentException("Grid reference '%s' is not a valid Channel Islands grid reference".format(gridRef))

  //Check grid ref is not below minimum preciesion
  val currentPrecision = getPrecision(gridRef)

  if (currentPrecision > 10000) throw new IllegalArgumentException("Grid reference precision must be 10Km or higher")

  //Normalise the precision to one of the allowable values
  val normalisedPrecision = if (precision != 0) getNormalisedPrecision(precision) else 0

  val outputGridRef = {

    if (normalisedPrecision > 0 &&  normalisedPrecision < currentPrecision) {
      throw ImportFailedException("Normailised precsion '%s' is greater then grid ref '%s' precision".format(normalisedPrecision, gridRef))
    }
    else if (normalisedPrecision > 0 && normalisedPrecision > currentPrecision) {
      decreaseGridPrecision(gridRef,normalisedPrecision)
    }
    else if (currentPrecision < 100) {
      decreaseGridPrecision(gridRef, 100)
    }
    else {
      gridRef
    }
  }

  def projection =  "ED50"

  def gridReference = outputGridRef

  def gridReferencePrecision = getPrecision(outputGridRef)

  def getLowerPrecisionGridRef(precision: Int) = new ChannelIslandGridSquareInfo(outputGridRef, precision)

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

  private def getTenFigGridRef(gridRef: String)= {

    val numerals =
      if (gridRef.matches(GridRefPatterns.channelIslandsDintyGridRef)) {
        val numericPart = getNumeralsFromGridRef(gridRef)
        expandDinty(numericPart)
      }
      else {
        getNumeralsFromGridRef(gridRef)
      }

    val letters = getLettersFromGridRef(gridRef)

    letters + padNumericPart(numerals, 10)
  }

  def getParentGridRef: Option[ChannelIslandGridSquareInfo] = {
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


  private def decreaseGridPrecision(gridRef: String, targetPrecision: Int) : String = {
    //If targetPrecision is 2000 decrease to DINTY grid ref
    if (targetPrecision == 2000) {
      computeDintyFromGridRef(gridRef)
    }
    //Else reduce to target grid ref
    else if (gridRef.matches(GridRefPatterns.channelIslandsDintyGridRef) && targetPrecision == 10000){
      //can only reduce this to 10000m
      gridRef.substring(0,4)
    }
    else if (targetPrecision == 100){
      trimGridDigits(gridRef, 6)
    }
    else if (targetPrecision == 1000){
      trimGridDigits(gridRef, 4)
    }
    else if (targetPrecision == 10000) {
      trimGridDigits(gridRef, 2)
    }
    else
    {
      throw new IllegalArgumentException("Invalid target precision")
    }
  }

  private def trimGridDigits(gridRefString: String, maxDigits: Int) = {
    var numericPart = getNumeralsFromGridRef(gridRef)
    var parts = numericPart.splitAt(numericPart.length / 2)
    var easting = parts._1.substring(0, maxDigits / 2)
    var northing = parts._2.substring(0, maxDigits / 2)
    var gridLetters = getLettersFromGridRef(gridRef)

    gridLetters + easting + northing
  }

  private def computeDintyFromGridRef(gridRef: String) = {
    if (gridRef.matches(GridRefPatterns.channelIslandsDintyGridRef)) {
      //already a DINTY grid ref
      gridRef
    }
    else {
      val numericPart = getNumeralsFromGridRef(gridRef)

      val gridLetters = getLettersFromGridRef(gridRef)
      val dintyPart = computeDinty(numericPart)

      gridLetters + dintyPart
    }
  }

  //Returns the grid reference precision in meters
  private def getPrecision(gridReference : String) = {
    if (gridReference.matches("""^W$""")) {
      500000
    }
    else if (gridReference.matches("""^W[AV]$""")) {
      100000
    }
    else if (gridReference.matches(GridRefPatterns.channelIslandsDintyGridRef)) {
      2000
    }
    else {
      //Otherwise the precision is inversely proportional to ten to the power of the number of digits
      val numerals = getNumeralsFromGridRef(gridReference).length
      (100000 / pow(10, (numerals / 2))).toInt
    }
  }

  private def getLettersFromGridRef(gridRef : String) = {
    gridRef.substring(0,2)
  }

  private def getNumeralsFromGridRef(gridRef : String) = {
    gridRef.substring(2, gridRef.length)
  }
}
