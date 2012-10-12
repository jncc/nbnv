package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.ImportFailedException
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.data.{Database, Repository}

class GridSquareInfoFactory @Inject()(db: Database) {

  /// Returns None for WGS84 points that don't lie within any supported grid system.
  def getByCoordinate(east: Double, north: Double, spatialReferenceSystem: String, gridReferencePrecision: Int) : Option[GridSquareInfo] = {

    val srs = if (spatialReferenceSystem == "4326") db.repo.getSRSForLatLong(east, north) else Some(spatialReferenceSystem)

    srs match {
      case None => None
      case Some("27700") => Some(BritishGridSquareInfo(east.toInt, north.toInt, gridReferencePrecision))
      case Some("23030") => Some(IrishGridSquareInfo(east.toInt, north.toInt, gridReferencePrecision))
      case Some("29903") => Some(ChannelIslandGridSquareInfo(east.toInt, north.toInt, gridReferencePrecision))
      case _ => throw new ImportFailedException("Unknown spatial referene system '%s'".format(spatialReferenceSystem))
    }
  }

  def getByGridRef(gridRef: String, gridReferenceType: String = "", gridReferencePrecision: Int = 0) : GridSquareInfo = {

    val gridType = if (gridReferenceType.isEmpty) determineGridRefType(gridRef)
                   else GridSystem(gridReferenceType)

    gridType match {
      case BritishGrid => BritishGridSquareInfo(gridRef, gridReferencePrecision)
      case IrishGrid   => IrishGridSquareInfo(gridRef, gridReferencePrecision)
      case ChannelGrid => ChannelIslandGridSquareInfo(gridRef, gridReferencePrecision)
      case UnknownGrid => throw new ImportFailedException("Unknown grid reference type '%s'".format(gridReferenceType))
    }
  }

  private def determineGridRefType(gridRef: String) = {

    if (gridRef.matches(GridRefPatterns.ukGridRef)) BritishGrid
    else if (gridRef.matches(GridRefPatterns.ukDintyGridRef)) BritishGrid
    else if (gridRef.matches(GridRefPatterns.irishGridRef)) IrishGrid
    else if (gridRef.matches(GridRefPatterns.irishDintyGrid)) IrishGrid
    else if (gridRef.matches(GridRefPatterns.channelIslandsGridRef)) ChannelGrid
    else if (gridRef.matches(GridRefPatterns.channelIslandsDintyGridRef)) ChannelGrid
    else throw new ImportFailedException("Grid reference type cannot be determined from grid ref '%s'".format(gridRef))

  }

}
