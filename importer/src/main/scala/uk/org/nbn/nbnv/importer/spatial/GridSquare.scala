package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.ImportFailedException

trait GridSquare {
  def projection : String
  def gridReference : String
  def gridReferencePrecision : Int
  def wgs84Polygon : String
  def getParentGridRef : Option[GridSquare]

  protected val dintyGridByCoord = Map (
    (0,8) -> "E", (2,8) -> "J", (4,8) -> "P", (6,8) -> "U", (8,8) -> "Z",
    (0,6) -> "D", (2,6) -> "I", (4,6) -> "N", (6,6) -> "T", (8,6) -> "Y",
    (0,4) -> "C", (2,4) -> "H", (4,4) -> "M", (6,4) -> "S", (8,4) -> "X",
    (0,2) -> "B", (2,2) -> "G", (4,2) -> "L", (6,2) -> "R", (8,2) -> "W",
    (0,0) -> "A", (2,0) -> "F", (4,0) -> "K", (6,0) -> "Q", (8,0) -> "V"
  )

  protected val dintyGridByLetter = dintyGridByCoord map {_.swap}

  protected def getDintyLeter(easting: Int, northing: Int) = {
    val dintyEasting = easting - (easting % 2)
    val dintyNorthing = northing - (northing % 2)

    dintyGridByCoord(dintyEasting, dintyNorthing)
  }


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
      throw new ImportFailedException("Bad precision entry > 10000 : %s".format(precision))
    }
  }



}
