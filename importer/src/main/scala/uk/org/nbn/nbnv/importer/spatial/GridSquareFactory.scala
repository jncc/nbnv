package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.ImportFailedException

class GridSquareFactory {
  def getGridSquare(gridRef: String, gridReferenceType: String = "", gridReferencePrecision: Int = 0) : GridSquare = {


    val gridType = if (gridReferenceType.isEmpty) {
                      getGridRefType(gridRef)
                    }
                    else {
                      gridReferenceType
                    }

    gridType match {
      case "OSGB36" =>  new BritishGridSquare(gridRef, gridReferencePrecision)
      case "OSGB" =>  new BritishGridSquare(gridRef, gridReferencePrecision)
      case "BNG"  =>  new BritishGridSquare(gridRef, gridReferencePrecision)
      case "OSI"  =>  new IrishGridSquare(gridRef, gridReferencePrecision)
      case "OSNI" =>  new IrishGridSquare(gridRef, gridReferencePrecision)
      case "ING" =>  new IrishGridSquare(gridRef, gridReferencePrecision)
      case "ED50" =>  new ChannelIslandGridSquare(gridRef, gridReferencePrecision)
      case "UTM"  =>  new ChannelIslandGridSquare(gridRef, gridReferencePrecision)
      case "CI"  =>  new ChannelIslandGridSquare(gridRef, gridReferencePrecision)
      case _      =>  throw new ImportFailedException("Unknown grid reference type '%s'".format(gridType))
    }
  }

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
