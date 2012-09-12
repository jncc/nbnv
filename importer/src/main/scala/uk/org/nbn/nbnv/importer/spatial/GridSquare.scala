package uk.org.nbn.nbnv.importer.spatial

import com.sun.javaws.exceptions.InvalidArgumentException


trait GridSquare {
  def gridReference : String
  def gridReferencePrecision : Int
  def wgs84Polygon : String
  def getParentGridRef : GridSquare
}
