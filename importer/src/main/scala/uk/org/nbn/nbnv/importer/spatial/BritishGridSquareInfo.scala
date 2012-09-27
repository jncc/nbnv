package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.ImportFailedException
import scala.math._

class BritishGridSquareInfo(gridRef : String, precision: Int = 0) extends GridSquareInfo {
  val majorBritishGridByLetter = Map (
    "H" -> (0,10), "J" -> (5,10),
    "N" -> (0,5), "0" -> (5,5),
    "S" -> (0,0), "T" -> (0,5)
  )

  val minorBritishGridByLetter = Map (
    "A" -> (0,4), "B" -> (1,4), "C" -> (2,4), "D" -> (3,4), "E" -> (4,4),
    "F" -> (0,3), "G" -> (1,3), "H" -> (2,3), "J" -> (3,3), "K" -> (4,3),
    "L" -> (0,2), "M" -> (1,2), "N" -> (2,2), "O" -> (3,2), "P" -> (4,2),
    "Q" -> (0,1), "R" -> (1,1), "S" -> (2,1), "T" -> (3,1), "U" -> (4,1),
    "V" -> (0,0), "W" -> (1,0), "X" -> (2,0), "Y" -> (3,0), "Z" -> (4,0)
  )

  //Check grid ref is uk grid ref
  if (gridRef.matches(GridRefPatterns.ukGridRef) == false
    && gridRef.matches(GridRefPatterns.ukDintyGridRef)  == false)
    throw new IllegalArgumentException("Grid reference '%s' is not a valid uk grid reference".format(gridRef))

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

  def projection = "OSGB36"

  def gridReference = outputGridRef

  def gridReferencePrecision = getPrecision(outputGridRef)

  def getLowerPrecisionGridRef(precision: Int) = new BritishGridSquareInfo(outputGridRef, precision)

  def sourceProjectionPolygon = {
    val gridSize = gridReferencePrecision

    val (easting, northing) = getEastingNorthing(gridRef)

    getPolygonFromGridSquareOrigin(easting, northing, gridSize)
  }

  def wgs84Polygon = {
    val gridSize = gridReferencePrecision

    val (easting, northing) = getEastingNorthing(gridRef)

    getWGS84PolygonFromGridSquareOrigin(easting, northing, gridSize, "27700")
  }

  def getParentGridRef: Option[BritishGridSquareInfo] = {
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

      Option(new BritishGridSquareInfo(parentGridReference))
    }
  }

  private def getEastingNorthing(gridRef: String) = {
    val g = getTenFigGridRef(gridRef)

    val (majorLetter, minorLetter) = getLettersFromGridRef(g).splitAt(1)
    val (majX, majY) = majorBritishGridByLetter(majorLetter)
    val (mnrX, mnrY) = minorBritishGridByLetter(minorLetter)

    val (e,n) = getNumeralsFromGridRef(g).splitAt(5)

    val easting = (majX + mnrX) * 100000 + e.toInt
    val northing = (majY + mnrY) * 100000 + n.toInt

    (easting, northing)
  }

  private def decreaseGridPrecision(gridRef: String, targetPrecision: Int) : String = {
    //If targetPrecision is 2000 decrease to DINTY grid ref
    if (targetPrecision == 2000) {
      computeDintyFromGridRef(gridRef)
    }
    //Else reduce to target grid ref
    else if (gridRef.matches(GridRefPatterns.ukDintyGridRef) && targetPrecision == 10000){
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

  private def getTenFigGridRef(gridRef: String)= {

    val numerals =
      if (gridRef.matches(GridRefPatterns.ukDintyGridRef)) {
        val numericPart = getNumeralsFromGridRef(gridRef)
        expandDinty(numericPart)
      }
      else {
        getNumeralsFromGridRef(gridRef)
      }

    val letters = getLettersFromGridRef(gridRef)

    letters + padNumericPart(numerals, 10)
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
    if (gridRef.matches(GridRefPatterns.ukDintyGridRef)) {
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
    if (gridReference.matches("""^[HNOST]$""")) {
      500000
    }
    else if (gridReference.matches("""^[HNOST][A-Z]$""")) {
      100000
    }
    else if (gridReference.matches(GridRefPatterns.ukDintyGridRef)) {
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
