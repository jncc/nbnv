package uk.org.nbn.nbnv.importer.spatial

class GridReferenceTypeMapper {
  def map(gridReferenceType: String) = {
    gridReferenceType match {
      case "OSGB36" =>  Option("OSGB36")
      case "OSGB" =>    Option("OSGB36")
      case "BNG"  =>    Option("OSGB36")
      case "OSI"  =>    Option("OSNI")
      case "OSNI" =>    Option("OSNI")
      case "ING" =>     Option("OSNI")
      case "ED50" =>    Option("ED50")
      case "UTM"  =>    Option("ED50")
      case "CI"  =>     Option("ED50")
      case _       =>   None
    }
  }

}
