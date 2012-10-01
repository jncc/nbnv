package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.ImportFailedException

class GridSquareInfoFactory {

  //todo: rename to getGridSquareByRef
  def getGridSquare(gridRef: String, gridReferenceType: String = "", gridReferencePrecision: Int = 0) : GridSquareInfo = {


    val gridType = if (gridReferenceType.isEmpty) {
                      getGridRefType(gridRef)
                    }
                    else {
                      gridReferenceType
                    }

    gridType match {
      case "OSGB36" =>  new BritishGridSquareInfo(gridRef, gridReferencePrecision)
      case "OSGB" =>  new BritishGridSquareInfo(gridRef, gridReferencePrecision)
      case "BNG"  =>  new BritishGridSquareInfo(gridRef, gridReferencePrecision)
      case "OSI"  =>  new IrishGridSquareInfo(gridRef, gridReferencePrecision)
      case "OSNI" =>  new IrishGridSquareInfo(gridRef, gridReferencePrecision)
      case "ING" =>  new IrishGridSquareInfo(gridRef, gridReferencePrecision)
      case "ED50" =>  new ChannelIslandGridSquareInfo(gridRef, gridReferencePrecision)
      case "UTM"  =>  new ChannelIslandGridSquareInfo(gridRef, gridReferencePrecision)
      case "CI"  =>  new ChannelIslandGridSquareInfo(gridRef, gridReferencePrecision)
      case _      =>  throw new ImportFailedException("Unknown grid reference type '%s'".format(gridType))
    }
  }

  //todo: implement getGridSquareByPoint
  //  Point will by either en or latlng
  //  Will have to determine if point is in british or irish grid.
  //  Return closes 100 m grid square.

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
