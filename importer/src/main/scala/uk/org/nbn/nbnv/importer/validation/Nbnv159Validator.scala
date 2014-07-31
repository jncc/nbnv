package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.spatial._
import uk.org.nbn.nbnv.importer.spatial.BritishGrid
import uk.org.nbn.nbnv.importer.spatial.IrishGrid
import uk.org.nbn.nbnv.importer.records.NbnRecord

//validate grid ref against type
class Nbnv159Validator () {

  val code = "NBNV-159"

  def validate(record: NbnRecord)  = {

    def success = {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: Grid refernce '%s' is valid for the specified spatial system".format(code, record.gridReferenceRaw.get)
        def reference = record.key
      }
    }

    def fail  = {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: The GridReference, %s is invalid for the supplied Projection".format(code, record.gridReferenceRaw.get)
        def reference = record.key
      }
    }

    val gridSystem = record.gridReferenceTypeRaw map {GridSystem(_)} orElse(
      record.srs map {GridSystem(_)})

    if(gridSystem.isDefined) {
      val gridReference = record.gridReferenceRaw.get

     gridSystem.get match {

        case BritishGrid => {
          if (gridReference.matches(GridRefPatterns.ukGridRef)
          || gridReference.matches(GridRefPatterns.ukDintyGridRef))
          success else fail
        }
        case IrishGrid => {
          if (gridReference.matches(GridRefPatterns.irishGridRef)
            || gridReference.matches(GridRefPatterns.irishDintyGrid))
            success else fail
        }
        case ChannelGrid => {
          if (gridReference.matches(GridRefPatterns.channelIslandsGridRef)
            || gridReference.matches(GridRefPatterns.channelIslandsDintyGridRef))
            success else fail
        }
        case UnknownGrid => {
          new Result {
            def level = ResultLevel.ERROR
            def message = "%s: The Projection is not one of the recognised types used in the NBN Exchange Format".format(code)
            def reference = record.key
          }
        }
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: The Projection is required for this record".format(code)
        def reference = record.key
      }
    }

  }


}
