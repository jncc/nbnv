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
  def validate(record: NbnRecord) = {
    val easting = record.east.get.toInt
    val northing =  record.north.get.toInt

    val point = record.feature.asInstanceOf[PointDef]

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
        def message = "NBNV-87: An unknonw grid reference system or srs has been supplied"
      }
    }
    else {
      val (result, error) =
        srs match {
          case 27700 => BritishGridSquareInfo.testEastingNorthing(easting, northing)
          case 29903 => IrishGridSquareInfo.testEastingNorthing(easting, northing)
          case 23030 => ChannelIslandGridSquareInfo.testEastingNorthing(easting, northing)
        }

      if (result == true) {
        new Result {
          def level = ResultLevel.DEBUG
          def reference = record.key
          def message = "NBNV-87: Validated: The easting and northing are valid for the specified SRS"
        }
      }
      else {
        new Result {
          def level = ResultLevel.ERROR
          def reference = record.key
          def message = error match {
            case Some(e) => "NBNV-87: " + e
            case None    => "NBNV-87: The easting and northing are invalid for the supplied srs"
          }
        }
      }
    }


  }
}
