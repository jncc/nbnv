package uk.org.nbn.nbnv.importer.spatial

class IrishGridSquareInfo(gridRef: String, precision: Int) extends GridSquareInfo {

  def projection =  "OSNI"

  def gridReference = null

  def gridReferencePrecision = 0

  def wgs84Polygon = null

  def getParentGridRef = None
}
