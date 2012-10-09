package uk.org.nbn.nbnv.importer.spatial

import scala.math._
import uk.org.nbn.nbnv.importer.ImportFailedException

object BritishGridSquareInfo {

  val majorBritishGridByLetter = Map (
    "H" -> (0,10), "J" -> (5,10),
    "N" -> (0,5), "0" -> (5,5),
    "S" -> (0,0), "T" -> (5,0)
  )

  val majorBritishGridByCoord = majorBritishGridByLetter map {_.swap}

  val minorBritishGridByLetter = Map (
    "A" -> (0,4), "B" -> (1,4), "C" -> (2,4), "D" -> (3,4), "E" -> (4,4),
    "F" -> (0,3), "G" -> (1,3), "H" -> (2,3), "J" -> (3,3), "K" -> (4,3),
    "L" -> (0,2), "M" -> (1,2), "N" -> (2,2), "O" -> (3,2), "P" -> (4,2),
    "Q" -> (0,1), "R" -> (1,1), "S" -> (2,1), "T" -> (3,1), "U" -> (4,1),
    "V" -> (0,0), "W" -> (1,0), "X" -> (2,0), "Y" -> (3,0), "Z" -> (4,0)
  )

  val minorBritishGridByCoord = minorBritishGridByLetter map {_.swap}

  def apply(east: Int, north: Int) : BritishGridSquareInfo = {
    BritishGridSquareInfo(east, north,0)
  }

  def apply(east: Int, north: Int, precision: Int) : BritishGridSquareInfo = {

    //Compute 100K grid square co-ordinate

    val e100k = (east - (east % 100000))
    val n100k = (north - (north % 100000))

    val sqX = e100k / 100000
    val sqY = n100k / 100000

    //Derive major (500K) grid letter
    val majX = sqX - (sqX % 5)
    val majY = sqY - (sqY % 5)

    if (majorBritishGridByCoord.get(majX, majY).isEmpty) throw new ImportFailedException("The easing and northing (%s,%s) are not within the british grid".format(east,north))

    val majLetter = majorBritishGridByCoord(majX, majY)

    //Determine minor (100K sub grid) grid letter
    val mnrX = sqX - majX
    val mnrY = sqY - majY

    val minLetter = minorBritishGridByCoord(mnrX, mnrY)

    if (minorBritishGridByCoord.get(mnrX, mnrY).isEmpty) throw new ImportFailedException("The easing and northing (%s,%s) are not within the british grid".format(east,north))

    var eastPart = (east - e100k).toString
    eastPart = "0" * (5 - eastPart.length) + eastPart

    var northPart = (north - n100k).toString
    northPart = "0" * (5 - northPart.length) + northPart


    val gridRef = majLetter + minLetter + eastPart + northPart

    new BritishGridSquareInfo(gridRef, precision)
  }

  def apply(gridRef : String) : BritishGridSquareInfo = {
    new BritishGridSquareInfo(gridRef)
  }

  def apply(gridRef : String, precision : Int) : BritishGridSquareInfo = {
    new BritishGridSquareInfo(gridRef, precision)
  }
}

class BritishGridSquareInfo(gridRef : String, precision: Int = 0) extends GridSquareInfo(gridRef, precision) {

  def projection = "OSGB36"

  def epsgCode = "27700"

  def getEastingNorthing = {
    val g = getTenFigGridRef(outputGridRef)

    val (majorLetter, minorLetter) = getLettersFromGridRef(g).splitAt(1)
    val (majX, majY) = BritishGridSquareInfo.majorBritishGridByLetter(majorLetter)
    val (mnrX, mnrY) = BritishGridSquareInfo.minorBritishGridByLetter(minorLetter)

    val (e,n) = getNumeralsFromGridRef(g).splitAt(5)

    val easting = (majX + mnrX)  * 100000 + e.toInt
    val northing = (majY + mnrY) * 100000 + n.toInt

    (easting, northing)
  }

  protected def create(gridRef: String, precision: Int = 0) = {
    new BritishGridSquareInfo(gridRef, precision)
  }

  protected def checkGridRef {
    if (gridRef.matches(GridRefPatterns.ukGridRef) == false
      && gridRef.matches(GridRefPatterns.ukDintyGridRef)  == false)
      throw new IllegalArgumentException("Grid reference '%s' is not a valid uk grid reference".format(gridRef))
  }

  protected def getDintyRegex = GridRefPatterns.ukDintyGridRef



  //Returns the grid reference precision in meters
  protected def getPrecision(gridReference : String) = {
    if (gridReference.matches("""^[HNOST]$""")) {
      500000
    }
    else if (gridReference.matches("""^[HNOST][A-Z]$""")) {
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

}
