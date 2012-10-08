package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.ImportFailedException
import org.geotools.geometry.GeneralDirectPosition
import org.geotools.referencing.ReferencingFactoryFinder
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory

abstract class GridSquareInfo(gridRef : String, precision: Int = 0) {

  def projection : String
  def epsgCode : String
  def getEastingNorthing : (Int, Int)

  protected def getLettersFromGridRef(gridRef: String) : String
  protected def getNumeralsFromGridRef(gridRef: String) : String
  protected def getDintyRegex : String
  protected def getPrecision(gridReference : String) : Int
  protected def checkGridRef
  protected def create(gridRef: String, precision: Int = 0) : GridSquareInfo

  val dintyGridByCoord = Map (
    (0,8) -> "E", (2,8) -> "J", (4,8) -> "P", (6,8) -> "U", (8,8) -> "Z",
    (0,6) -> "D", (2,6) -> "I", (4,6) -> "N", (6,6) -> "T", (8,6) -> "Y",
    (0,4) -> "C", (2,4) -> "H", (4,4) -> "M", (6,4) -> "S", (8,4) -> "X",
    (0,2) -> "B", (2,2) -> "G", (4,2) -> "L", (6,2) -> "R", (8,2) -> "W",
    (0,0) -> "A", (2,0) -> "F", (4,0) -> "K", (6,0) -> "Q", (8,0) -> "V"
  )

  val dintyGridByLetter = dintyGridByCoord map {_.swap}

  //Check grid ref
  checkGridRef

  //Check grid ref is not below minimum precision
  val currentPrecision = getPrecision(gridRef)

  if (currentPrecision > 10000) throw new IllegalArgumentException("Grid reference precision must be 10Km or higher")

  //Normalise the precision to one of the allowable values
  val normalisedPrecision = if (precision != 0) getNormalisedPrecision(precision) else 0

  val outputGridRef = {

    if (normalisedPrecision > 0 &&  normalisedPrecision < currentPrecision) {
      throw ImportFailedException("Normailised precsion '%s' is greater then grid ref '%s' precision".format(normalisedPrecision, gridRef))
    }
    else if (normalisedPrecision > 0 && normalisedPrecision > currentPrecision) {
      decreaseGridPrecision(gridRef.toUpperCase,normalisedPrecision)
    }
    else if (currentPrecision < 100) {
      decreaseGridPrecision(gridRef.toUpperCase, 100)
    }
    else {
      gridRef.toUpperCase
    }
  }

  def gridReference = outputGridRef

  def gridReferencePrecision = getPrecision(outputGridRef)

  def getParentGridSquareInfo: Option[GridSquareInfo] = {
    val parentGridRef = getParentGridRef
    if (parentGridRef.isEmpty) {
      None
    }
    else {
      Some(create(parentGridRef))
    }
  }

  def getLowerPrecisionGridSquareInfo(precision: Int) = {
    if (precision <= gridReferencePrecision) {
      this
    }
    else {
      create(outputGridRef, precision)
    }
  }

  def sourceProjectionPolygon = {
    val (easting, northing) = getEastingNorthing

    val gridSize = gridReferencePrecision

    getPolygonFromGridSquareOrigin(easting, northing, gridSize)
  }

  def wgs84Polygon = {
    val (easting, northing) = getEastingNorthing

    val gridSize = gridReferencePrecision

    getWGS84PolygonFromGridSquareOrigin(easting, northing, gridSize, epsgCode)
  }

  protected def getTenFigGridRef(gridRef: String)= {

    val numerals =
      if (gridRef.matches(getDintyRegex)) {
        val numericPart = getNumeralsFromGridRef(gridRef)
        expandDinty(numericPart)
      }
      else {
        getNumeralsFromGridRef(gridRef)
      }

    val letters = getLettersFromGridRef(gridRef)

    letters + padNumericPart(numerals, 10)
  }

  private def getDintyLeter(easting: Int, northing: Int) = {
    val dintyEasting = easting - (easting % 2)
    val dintyNorthing = northing - (northing % 2)

    dintyGridByCoord(dintyEasting, dintyNorthing)
  }

  private def trimGridDigits(gridRef: String, maxDigits: Int) = {
    var numericPart = getNumeralsFromGridRef(gridRef)
    var parts = numericPart.splitAt(numericPart.length / 2)
    var easting = parts._1.substring(0, maxDigits / 2)
    var northing = parts._2.substring(0, maxDigits / 2)
    var gridLetters = getLettersFromGridRef(gridRef)

    gridLetters + easting + northing
  }

  private def decreaseGridPrecision(gridRef: String, targetPrecision: Int) : String = {
    //If targetPrecision is 2000 decrease to DINTY grid ref
    if (targetPrecision == 2000) {
      computeDintyFromGridRef(gridRef)
    }
    //Else reduce to target grid ref
    else if (gridRef.matches(getDintyRegex) && targetPrecision == 10000){
      val gridLetters = getLettersFromGridRef(gridRef)
      val numericPart = getNumeralsFromGridRef(gridRef)
      gridLetters + numericPart.substring(0,2)
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

  private def computeDintyFromGridRef(gridRef: String) = {
    if (gridRef.matches(getDintyRegex)) {
      //already a DINTY grid ref
      gridRef
    }
    else {
      val numericPart = getNumeralsFromGridRef(gridRef)
      val numericComponents = numericPart.splitAt(numericPart.length / 2)
      //gives 3
      val dintyEasting = numericComponents._1.substring(1,2).toInt
      //gives 6
      val dintyNorthing = numericComponents._2.substring(1,2).toInt
      //gives I (2, 6)
      val dintyLetter = getDintyLeter(dintyEasting, dintyNorthing)
      //gives 2
      val easting = numericComponents._1.substring(0,1)
      //gives 3
      val northing = numericComponents._2.substring(0,1)

      val gridLetters = getLettersFromGridRef(gridRef)

      //gives 23I
      gridLetters + easting + northing + dintyLetter
    }
  }

  private def padNumericPart(numericPart: String, padTo: Int) = {
    if (numericPart.length == padTo) {
      numericPart
    }
    else {
      val numericParts = numericPart.splitAt(numericPart.length / 2)
      val padLength = (padTo - numericPart.length) / 2
      val padString = "0" * padLength

      numericParts._1 + padString + numericParts._2 + padString
    }
  }

  private def getParentGridRef = {
    if (gridReferencePrecision == 10000) {
      ""
    }
    else {
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
        throw new ImportFailedException("Current grid reference has an invalid precision")
      }
    }
  }

  private def expandDinty(numericPart: String) = {
    //eg 32C
    //gives C
    val dintyLetter = numericPart.substring(2,3)
    //gives (0,4)
    val coordinates = dintyGridByLetter(dintyLetter)
    //gives (3,2)
    val numericParts = numericPart.substring(0,2).splitAt(1)
    //gives 3024
    numericParts._1 + coordinates._1 + numericParts._2 + coordinates._2
  }


  private def getNormalisedPrecision(precision : Int) = {
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

  private def getPolygonFromGridSquareOrigin(easting: Int, northing: Int, gridSize: Int) = {
    val bl = (easting, northing)
    val br = (easting + gridSize, northing)
    val tl = (easting, northing + gridSize)
    val tr = (easting + gridSize, northing + gridSize)

    "POLYGON((" + bl._1 + " " + bl._2 + ", " +
      tl._1 + " " + tl._2 + ", " +
      tr._1 + " " + tr._2 + ", " +
      br._1 + " " + br._2 + ", " +
      bl._1 + " " + bl._2 + "))"
  }

  private def getWGS84PolygonFromGridSquareOrigin(easting: Int, northing: Int, gridSize: Int, epsgCode: String) = {
    val blGdp = new GeneralDirectPosition(easting, northing)
    val brGdp = new GeneralDirectPosition(easting + gridSize, northing)
    val tlGdp = new GeneralDirectPosition(easting, northing + gridSize)
    val trGdp = new GeneralDirectPosition(easting + gridSize, northing + gridSize)

    //Get the Source CRS to WGS84 transformation operation
    val crsFac = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG",null)
    val wgs84crs = crsFac.createCoordinateReferenceSystem("4326")
    val SourceCrs = crsFac.createCoordinateReferenceSystem(epsgCode)
    val transformer = new DefaultCoordinateOperationFactory().createOperation(SourceCrs, wgs84crs).getMathTransform

    //Get the coordinates in WGS84 lat lng
    val bl = transformer.transform(blGdp, blGdp).getCoordinates
    val br = transformer.transform(brGdp, brGdp).getCoordinates
    val tl = transformer.transform(tlGdp, tlGdp).getCoordinates
    val tr = transformer.transform(trGdp, trGdp).getCoordinates

    "POLYGON((" + bl(1) + " " + bl(0) + ", " +
      tl(1) + " " + tl(0) + ", " +
      tr(1) + " " + tr(0) + ", " +
      br(1) + " " + br(0) + ", " +
      bl(1) + " " + bl(0) + "))"
  }
}
