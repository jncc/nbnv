package uk.org.nbn.nbnv.importer.spatial

import uk.me.jstott.jcoord.OSRef
import uk.org.nbn.nbnv.importer.ImportFailedException
import scala.math._
import uk.me.jstott.jcoord.datum.WGS84Datum

class BritishGridSquareInfo(gridRef : String, precision: Int = 0) extends GridSquareInfo {

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

  //todo: implement soucePolygon
  def sourcePolygon = null

  def wgs84Polygon = {
    val gridSize = gridReferencePrecision

    val paddedGridRef = getSixFigGridRef(outputGridRef)
    //bottom left co-ordinate
    val blRef = new OSRef(paddedGridRef)
    //bottom Right coordiante
    val brRef = new OSRef(blRef.getEasting + gridSize, blRef.getNorthing)
    //top left coordiante
    val tlRef = new OSRef(blRef.getEasting, blRef.getNorthing + gridSize)
    //top right coordinate
    val trRef = new OSRef(blRef.getEasting + gridSize, blRef.getNorthing + gridSize)

    //Get lat long in OSGB36
    //bottom left coordinate
    val bl = blRef.toLatLng
    //bottom right coordinate
    val br = brRef.toLatLng
    //top left coordinate
    val tl = tlRef.toLatLng
    //top right coordinate
    val tr = trRef.toLatLng

    //Reproject to WGS84
    bl.toWGS84
    br.toWGS84
    tl.toWGS84
    tr.toWGS84

    //Compose and return WKT
    "POLYGON((" + bl.getLongitude + " " + bl.getLatitude + ", " +
      tl.getLongitude + " " + tl.getLatitude + ", " +
      tr.getLongitude + " " + tr.getLatitude + ", " +
      br.getLongitude + " " + br.getLatitude + ", " +
      bl.getLongitude + " " + bl.getLatitude + "))"
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

  private def getSixFigGridRef(gridRef: String)= {

    val numerals =
      if (gridRef.matches(GridRefPatterns.ukDintyGridRef)) {
        //eg TL32C
        //gives 32C
        val numericPart = getNumeralsFromGridRef(gridRef)
        //gives C
        val dintyLetter = numericPart.substring(2,3)
        //gives (0,4)
        val coordinates = dintyGridByLetter(dintyLetter)
        //gives (3,2)
        val numericParts = numericPart.substring(0,2).splitAt(1)
        //gives 3024
        numericParts._1 + coordinates._1 + numericParts._2 + coordinates._2
      }
      else {
        getNumeralsFromGridRef(gridRef)
      }

    if (numerals.length == 6) {
      gridRef
    }
    else {
      val numericParts = numerals.splitAt(numerals.length / 2)
      val padLength = (6 - numerals.length) / 2
      val padString = "0" * padLength
      val letters = getLettersFromGridRef(gridRef)

      letters + numericParts._1 + padString + numericParts._2 + padString
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
    if (gridRef.matches(GridRefPatterns.ukDintyGridRef)) {
      //already a DINTY grid ref
      gridRef
    }
    else {
      //eg gridRef TL234369
      //gives 234369
      val numericPart = getNumeralsFromGridRef(gridRef) //gives 234369
      //gives (234,369)
      val numericComponents = numericPart.splitAt(numericPart.length / 2)
      //gives 3
      val dintyEasting = numericComponents._1.substring(1,2).toInt
      //gives 6
      val dintyNorthing = numericComponents._2.substring(1,2).toInt
      //gives I (2, 6)
      val dintyLetter = getDintyLeter(dintyEasting, dintyNorthing)

      //gives TL
      val gridLetters = getLettersFromGridRef(gridRef)
      //gives 2
      val easting = numericComponents._1.substring(0,1)
      //gives 3
      val northing = numericComponents._2.substring(0,1)

      //TL23I
      gridLetters + easting + northing + dintyLetter
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
