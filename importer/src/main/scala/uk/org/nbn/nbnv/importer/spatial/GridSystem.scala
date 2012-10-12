package uk.org.nbn.nbnv.importer.spatial

abstract class GridSystem
case class BritishGrid() extends GridSystem
case class IrishGrid() extends GridSystem
case class ChannelGrid() extends GridSystem
case class UnknownGrid() extends GridSystem

object GridSystem {
  def apply(s: String) = s.toUpperCase match {
    case "OSGB36" =>  BritishGrid
    case "OSGB" =>    BritishGrid
    case "BNG"  =>    BritishGrid
    case "OSI"  =>    IrishGrid
    case "OSNI" =>    IrishGrid
    case "ING" =>     IrishGrid
    case "ED50" =>    ChannelGrid
    case "UTM"  =>    ChannelGrid
    case "CI"  =>     ChannelGrid
    case _       =>   UnknownGrid
  }
}
