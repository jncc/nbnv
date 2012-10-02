package uk.org.nbn.nbnv.importer.spatial

object TestResources {
  //matches a properly formed WKT 4 point polygon including checking that first and last points are the same
  def polygonWKTRegex = """^POLYGON\(\((\d+(\.\d+)?)\s(\d+(\.\d+)?),\d+(\.\d+)?\s\d+(\.\d+)?,\d+(\.\d+)?\s\d+(\.\d+)?,\d+(\.\d+)?\s\d+(\.\d+)?,\1\s\3\)\)$"""
}
