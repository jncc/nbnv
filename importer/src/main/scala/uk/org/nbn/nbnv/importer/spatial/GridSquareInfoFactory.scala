package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.ImportFailedException
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.data.Repository

class GridSquareInfoFactory @Inject()(repo: Repository) {
  def getByCoordinate(east: Double, north: Double, spatialReferenceSystem: String, gridReferencePrecision: Int) : GridSquareInfo = {

    val srs = if (spatialReferenceSystem == "4326") repo.getSRSForLatLong(east, north) else spatialReferenceSystem

    srs match {
      case "27700" => BritishGridSquareInfo(east.toInt, north.toInt, gridReferencePrecision)
      case "23030" => IrishGridSquareInfo(east.toInt, north.toInt, gridReferencePrecision)
      case "29903" => ChannelIslandGridSquareInfo(east.toInt, north.toInt, gridReferencePrecision)
      case _ => throw new ImportFailedException("Unknown spatial referene system '%s'".format(spatialReferenceSystem))
     }
  }

  def getByGridRef(gridRef: String, gridReferenceType: String = "", gridReferencePrecision: Int = 0) : GridSquareInfo = {


    val gridType = if (gridReferenceType.isEmpty) {
                      getGridRefType(gridRef)
                    }
                    else {
                      gridReferenceType
                    }

    gridType match {
      case "OSGB36" =>  BritishGridSquareInfo(gridRef, gridReferencePrecision)
      case "OSGB" =>    BritishGridSquareInfo(gridRef, gridReferencePrecision)
      case "BNG"  =>    BritishGridSquareInfo(gridRef, gridReferencePrecision)
      case "OSI"  =>    IrishGridSquareInfo(gridRef, gridReferencePrecision)
      case "OSNI" =>    IrishGridSquareInfo(gridRef, gridReferencePrecision)
      case "ING" =>     IrishGridSquareInfo(gridRef, gridReferencePrecision)
      case "ED50" =>    ChannelIslandGridSquareInfo(gridRef, gridReferencePrecision)
      case "UTM"  =>    ChannelIslandGridSquareInfo(gridRef, gridReferencePrecision)
      case "CI"  =>     ChannelIslandGridSquareInfo(gridRef, gridReferencePrecision)
      case _      =>    throw new ImportFailedException("Unknown grid reference type '%s'".format(gridType))
    }
  }

  private def getGridRefType(gridRef: String) = {

    if (gridRef.matches(GridRefPatterns.ukGridRef)) "OSGB36"
    else if (gridRef.matches(GridRefPatterns.ukDintyGridRef)) "OSGB36"
    else if (gridRef.matches(GridRefPatterns.irishGridRef)) "OSNI"
    else if (gridRef.matches(GridRefPatterns.irishDintyGrid)) "OSNI"
    else if (gridRef.matches(GridRefPatterns.channelIslandsGridRef)) "ED50"
    else if (gridRef.matches(GridRefPatterns.channelIslandsDintyGridRef)) "ED50"
    else throw new ImportFailedException("Grid refernce type cannot be determined from grid ref '%s'".format(gridRef))

  }

}
