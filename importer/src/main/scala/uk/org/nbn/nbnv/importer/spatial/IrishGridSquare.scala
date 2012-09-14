package uk.org.nbn.nbnv.importer.spatial

class IrishGridSquare(gridRef: String, precision: Int) extends GridSquare {

  def projection =  "OSNI"

  def gridReference = null

  def gridReferencePrecision = 0

  def wgs84Polygon = null

  def getParentGridRef = None
}
