package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.{GridTypeDef, SrsDef, PointDef, NbnRecord}
import uk.org.nbn.nbnv.importer.spatial._
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.records.GridTypeDef
import scala.Some
import uk.org.nbn.nbnv.importer.records.SrsDef
import uk.org.nbn.nbnv.importer.records.PointDef
import javax.swing.event.ChangeEvent


//todo refactor this sh*t code
class Nbnv87Validator {
  val code="NBNV-87"

  def validate(record: NbnRecord) = {
    val point = record.feature.asInstanceOf[PointDef]

    val easting = point.east.toInt
    val northing =  point.north.toInt

    val srs = point.sys match {
      case SrsDef(srs) => srs
      case GridTypeDef(gridType) => GridSystem(gridType) match {
        case BritishGrid => 27700
        case IrishGrid => 29903
        case ChannelGrid => 23030
        case UnknownGrid => -1
      }
    }

    if (srs == -1) {
      new Result {
        def level = ResultLevel.ERROR
        def reference = record.key
        def message = "%s: The Projection is not one of the recognised types used in the NBN Exchange Format".format(code)
      }
    }
    else {
      val (result, error) =
        srs match {
          case 27700 => BritishGridSquareInfo.IsValidEastingNorthing(easting, northing)
          case 29903 => IrishGridSquareInfo.IsValidEastingNorthing(easting, northing)
          case 23030 => ChannelIslandGridSquareInfo.IsValidEastingNorthing(easting, northing)
        }

      if (result == true) {
        new Result {
          def level = ResultLevel.DEBUG
          def reference = record.key
          def message = "%s: Validated: The easting and northing are valid for the specified SRS".format(code)
        }
      }
      else {
        new Result {
          def level = ResultLevel.ERROR
          def reference = record.key
          def message = error match {
            case Some(e) => "%s: %s".format(code, e)
            case None    => "%s: The Easting and Northing are invalid for the supplied Projection".format(code)
          }
        }
      }
    }


  }
}
