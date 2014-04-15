package uk.org.nbn.nbnv.importer.spatial

import uk.org.nbn.nbnv.importer.BadDataException
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.data.Database
import uk.org.nbn.nbnv.importer.records.{GridTypeDef, SrsDef, GridRefDef, PointDef}
import org.apache.log4j.Logger

class GridSquareInfoFactory @Inject()(db: Database) {

  /// Returns None for WGS84 points that don't lie within any supported grid system.
  def getByCoordinate(value: PointDef) : Option[GridSquareInfo] = {

    val srs =
      value.sys match {
        case SrsDef(srs) => srs
        case GridTypeDef(gridType) => GridSystem(gridType) match {
          case BritishGrid => 27700
          case IrishGrid => 29903
          case ChannelGrid => 23030
          case UnknownGrid => throw new BadDataException("invalid grid type")
        }
      }

    if (srs == 4326) {
      val longitude = value.east
      val latitude = value.north
      val targetSrs = db.repo.getSRSForLatLong(longitude, latitude)

      try {
        targetSrs match {
          case Some(27700) => Some(BritishGridSquareInfo(latitude, longitude, value.prec))
          case Some(29903) => Some(IrishGridSquareInfo(latitude, longitude, value.prec))
          case Some(23030) => Some(ChannelIslandGridSquareInfo(latitude, longitude, value.prec))
          case None => None
          case _ => throw new BadDataException("Database identified unhandled spatial reference system")
        }
      }
      catch {
        case ife: BadDataException => {
          None
        } //Lat long may fall in the grid ref system but grid ref may not be in the valid rage.
        case e: Throwable => throw e
      }

    }
    else {
      srs match {
        case 27700 => Some(BritishGridSquareInfo(value.east.toInt, value.north.toInt, value.prec))
        case 29903 => Some(IrishGridSquareInfo(value.east.toInt, value.north.toInt, value.prec))
        case 23030 => Some(ChannelIslandGridSquareInfo(value.east.toInt, value.north.toInt, value.prec))
        case _ => throw new BadDataException("Unknown spatial reference system")
      }
    }
  }

  def getByGridRef(value: GridRefDef) : GridSquareInfo = {

    val gridType =
      value.sys match {
        case None => determineGridRefType(value.ref)
        case Some(GridTypeDef(gridType)) => GridSystem(gridType)
        case Some(SrsDef(srs)) => GridSystem(srs)
      }

    gridType match {
      case BritishGrid => BritishGridSquareInfo(value.ref, value.prec)
      case IrishGrid   => IrishGridSquareInfo(value.ref, value.prec)
      case ChannelGrid => ChannelIslandGridSquareInfo(value.ref, value.prec)
      case UnknownGrid => throw new BadDataException("Unknown grid reference type")
    }
  }

  private def determineGridRefType(gridRef: String) = {

    if (gridRef.matches(GridRefPatterns.ukGridRef)) BritishGrid
    else if (gridRef.matches(GridRefPatterns.ukDintyGridRef)) BritishGrid
    else if (gridRef.matches(GridRefPatterns.irishGridRef)) IrishGrid
    else if (gridRef.matches(GridRefPatterns.irishDintyGrid)) IrishGrid
    else if (gridRef.matches(GridRefPatterns.channelIslandsGridRef)) ChannelGrid
    else if (gridRef.matches(GridRefPatterns.channelIslandsDintyGridRef)) ChannelGrid
    else throw new BadDataException("Grid reference type cannot be determined from grid ref '%s'".format(gridRef))

  }

}
