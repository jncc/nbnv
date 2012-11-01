package uk.org.nbn.nbnv.importer.records


abstract sealed class LocationDef

case class FeatureDef(key: String) extends LocationDef
case class GridRefDef(ref: String, sys: Option[SpatialSystemDef], prec: Option[Int]) extends LocationDef
case class PointDef(east: Double, north: Double, sys: SpatialSystemDef, prec: Option[Int]) extends LocationDef


abstract sealed class SpatialSystemDef

case class SrsDef(code: Int) extends SpatialSystemDef
case class GridTypeDef(code: String) extends SpatialSystemDef

