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
      case "OSBG" =>  new BritishGridSquare(gridRef, gridReferencePrecision)
      case "BNI"  =>  new BritishGridSquare(gridRef, gridReferencePrecision)
      case "OSI"  =>  new IrishGridSquare(gridRef, gridReferencePrecision)
      case "OSNI" =>  new IrishGridSquare(gridRef, gridReferencePrecision)
      case "ED50" =>  new ChannelIslandGridSquare(gridRef, gridReferencePrecision)
      case "UTM"  =>  new ChannelIslandGridSquare(gridRef, gridReferencePrecision)
      case _      =>  throw new ImportFailedException("Unknown grid reference type '%s'".format(gridType))
    }
  }

  private def getGridRefType(gridRef: String) = {
    //Case insensitve (?i) regex to match each grid ref
    val channelIslandsGridRef = """(?i)^[W][A-Z](\d\d)*$""".r
    val ukGridRef =  """(?i)^[HNOST][A-Z](\d\d)*$""".r
    val irishGridRef =  """(?i)^[A-HJ-Z](\d\d)*""".r

    gridRef match {
      case channelIslandsGridRef() => "ED50"
      case ukGridRef() => "OSGB"
      case irishGridRef() => "OSI"
      case _ => throw new ImportFailedException("Grid refernce type cannot be determined from grid ref '%s'".format(gridRef))
    }
  }

}
