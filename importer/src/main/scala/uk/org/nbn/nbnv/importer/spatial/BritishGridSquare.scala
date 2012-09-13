package uk.org.nbn.nbnv.importer.spatial

import uk.me.jstott.jcoord.OSRef
import uk.org.nbn.nbnv.importer.ImportFailedException
import com.sun.javaws.exceptions.InvalidArgumentException

class BritishGridSquare(gridRef : String, precision: Int = 0) extends GridSquare {

  if (gridRef.matches(GridRefPatterns.ukGridRef) == false
    && gridRef.matches(GridRefPatterns.ukDintyGridRef)  == false)
    throw new IllegalArgumentException("Grid reference '%s' is not a valid uk grid reference".format(gridRef))

  //Normalise the precision to one of the allowable values
  val normalisedPrecision = normalisePrecision(precision)

  val outputGridRef = {
    val currentPrecision = getPrecision(gridRef)

    if (normalisedPrecision < currentPrecision) {
      throw ImportFailedException("Normailised precsion '%s' is greater then grid ref '%s' precision".format(normalisedPrecision, gridRef))
    }
    else if (normalisedPrecision > currentPrecision) {
      decreaseGridPrecision(gridRef,normalisedPrecision)
    }
    else if (currentPrecision < 100) {
      decreaseGridPrecision(gridRef, 100)
    }
    else {
      gridRef
    }
  }

  def gridReference = outputGridRef

  def gridReferencePrecision = getPrecision(outputGridRef)

  def wgs84Polygon = null

  def getParentGridRef = null

  private def decreaseGridPrecision(gridRef: String, targetPrecision: Int) = {
    //If targetPrecision is 2000 decrease to DINTY grid ref
    if (targetPrecision == 2000) {
      computeDintyFromGridRef(gridRef)
    }
    //Else reduce to target grid ref
    else if (gridRef.matches(GridRefPatterns.ukDintyGridRef)){
      //todo: dinty grid ref precision reduction do something here
    }
    else {
      //todo: reduce precision of a normal grid ref to a normal precision
    }

    //todo: get rid of this null
    null
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
      val gridLetters = gridRef.substring(0,2)
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
    if (gridRef.matches("""^[HNOST]$""")) {
      500000
    }
    else if (gridRef.matches("""^[HNOST][A-Z]$""")) {
      100000
    }
    else if (gridRef.matches(GridRefPatterns.ukDintyGridRef)) {
      2000
    }
    else {
      //Otherwise the precision is inversely proportional to ten to the power of the number of digits
      var numerals = getNumeralsFromGridRef(gridRef).length
      1000000 / 10 ^ (numerals / 2)
    }
  }

  private def getNumeralsFromGridRef(gridRef : String) = {
    gridRef.substring(2, gridRef.length)
  }
}
