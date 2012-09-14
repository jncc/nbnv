package uk.org.nbn.nbnv.importer.spatial

class ChannelIslandGridSquare(gridRef: String, precision: Int) extends GridSquare {

  def projection =  "ED50"

  def gridReference = null

  def gridReferencePrecision = 0

  def wgs84Polygon = null

  def getParentGridRef = None
}
