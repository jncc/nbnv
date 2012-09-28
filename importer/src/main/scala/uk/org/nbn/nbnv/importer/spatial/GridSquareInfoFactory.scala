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
    //Case insensitve (?i) regex to match each grid ref
    val channelIslandsGridRef = GridRefPatterns.channelIslandsGridRef.r
    val channelIslandsDintyGridRef = GridRefPatterns.channelIslandsDintyGridRef.r
    val ukGridRef = GridRefPatterns.ukGridRef.r
    val ukDintyGridRef = GridRefPatterns.ukDintyGridRef.r
    val irishGridRef =  GridRefPatterns.irishGridRef.r
    val irishDintyGridRef = GridRefPatterns.irishDintyGrid.r

    gridRef match {
      case channelIslandsGridRef() => "ED50"
      case channelIslandsDintyGridRef() => "ED50"
      case ukGridRef() => "OSGB36"
      case ukDintyGridRef() => "OSGB36"
      case irishGridRef() => "OSNI"
      case irishDintyGridRef() => "OSNI"
      case _ => throw new ImportFailedException("Grid refernce type cannot be determined from grid ref '%s'".format(gridRef))
    }
  }

}
