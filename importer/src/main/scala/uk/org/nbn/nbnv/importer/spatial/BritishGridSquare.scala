package uk.org.nbn.nbnv.importer.spatial

import uk.me.jstott.jcoord.OSRef
import uk.org.nbn.nbnv.importer.ImportFailedException

class BritishGridSquare(gridRef : String, precision: Int = 0) extends GridSquare {

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
    else {

    }
  }

  private def computeDintyFromGridRef(gridRef: String) = {
    if (gridRef.matches(GridRefPatterns.ukDintyGridRef)) {
      gridRef
    }
    else {

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
      var numerals = gridRef.substring(2, gridRef.length - 1).length
      1000000 / 10 ^ (numerals / 2)
    }
  }
}
