package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.ImportFailedException

trait GridSquare {
  def gridReference : String
  def gridReferencePrecision : Int
  def wgs84Polygon : String
  def getParentGridRef : GridSquare
  
  protected val dintyNorthing = Map(
    "E"-> 8, "J"-> 8, "P"-> 8, "U"-> 8, "Z"-> 8,
    "D"-> 6, "I"-> 6, "N"-> 6, "T"-> 6, "Y"-> 6,
    "C"-> 4, "H"-> 4, "M"-> 4, "S"-> 4, "X"-> 4,
    "B"-> 2, "G"-> 2, "L"-> 2, "R"-> 2, "W"-> 2,
    "A"-> 0, "F"-> 0, "K"-> 0, "Q"-> 0, "V"-> 0
  )

  protected val dintyEasting = Map(
    "E"-> 0, "J"-> 2, "P"-> 4, "U"-> 6, "Z"-> 8,
    "D"-> 0, "I"-> 2, "N"-> 4, "T"-> 6, "Y"-> 8,
    "C"-> 0, "H"-> 2, "M"-> 4, "S"-> 6, "X"-> 8,
    "B"-> 0, "G"-> 2, "L"-> 4, "R"-> 6, "W"-> 8,
    "A"-> 0, "F"-> 2, "K"-> 4, "Q"-> 6, "V"-> 8
  )

  protected def normalisePrecision(precision : Int) = {
    if (precision <= 100) {
      100
    }
    else if (precision <= 1000) {
      1000
    }
    else if (precision <= 2000) {
      2000
    }
    else if (precision <= 10000) {
      10000
    }
    else {
      throw new ImportFailedException("Bad precision entry >10000 : " + precision)
    }
  }



}
