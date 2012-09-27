package uk.org.nbn.nbnv.importer.spatial

import math._
import uk.org.nbn.nbnv.importer.ImportFailedException

class IrishGridSquareInfo(gridRef: String, precision: Int = 0) extends GridSquareInfo(gridRef, precision) {

  val irishGridByLetter = Map (
    "A" -> (0,4), "B" -> (1,4), "C" -> (2,4), "D" -> (3,4), "E" -> (4,4),
    "F" -> (0,3), "G" -> (1,3), "H" -> (2,3), "J" -> (3,3), "K" -> (4,3),
    "L" -> (0,2), "M" -> (1,2), "N" -> (2,2), "O" -> (3,2), "P" -> (4,2),
    "Q" -> (0,1), "R" -> (1,1), "S" -> (2,1), "T" -> (3,1), "U" -> (4,1),
    "V" -> (0,0), "W" -> (1,0), "X" -> (2,0), "Y" -> (3,0), "Z" -> (4,0)
  )

  def projection =  "OSNI"

  def getLowerPrecisionGridRef(precision: Int) = new IrishGridSquareInfo(outputGridRef, precision)

  def sourceProjectionPolygon = {
    val (easting, northing) = getEastingNorthing(outputGridRef)

    val gridSize = gridReferencePrecision

    getPolygonFromGridSquareOrigin(easting, northing, gridSize)
  }

  def wgs84Polygon = {

    val (easting, northing) = getEastingNorthing(outputGridRef)

    val gridSize = gridReferencePrecision

    getWGS84PolygonFromGridSquareOrigin(easting, northing, gridSize, "29903")
  }

  def getParentGridRef: Option[IrishGridSquareInfo] = {
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

      Option(new IrishGridSquareInfo(parentGridReference))
    }
  }

  protected def checkGridRef {
    if (gridRef.matches(GridRefPatterns.irishGridRef) == false
      && gridRef.matches(GridRefPatterns.irishDintyGrid)  == false)
      throw new IllegalArgumentException("Grid reference '%s' is not a valid Irish grid reference".format(gridRef))
  }

  private def getEastingNorthing(gridRef: String) = {
    val g = getTenFigGridRef(gridRef)

    val (x, y) = irishGridByLetter(getLettersFromGridRef(g))
    val (e, n) = getNumeralsFromGridRef(g).splitAt(5)

    (x * 100000 + e.toInt, y * 100000 + n.toInt)
  }

  //Returns the grid reference precision in meters
  protected def getPrecision(gridReference : String) = {
    if (gridReference.matches("""^[A-HJ-Z]$""")) {
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
    gridRef.substring(0,1)
  }

  protected def getNumeralsFromGridRef(gridRef : String) = {
    gridRef.substring(1, gridRef.length)
  }

  protected def getDintyRegex = GridRefPatterns.irishDintyGrid
}
