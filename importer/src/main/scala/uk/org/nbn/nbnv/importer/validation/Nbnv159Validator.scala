package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.spatial._
import uk.org.nbn.nbnv.importer.spatial.BritishGrid
import uk.org.nbn.nbnv.importer.spatial.IrishGrid
import uk.org.nbn.nbnv.importer.records.NbnRecord

//validate grid ref against type
class Nbnv159Validator () {
  def validate(record: NbnRecord)  = {

    def success = {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-159: Validated: Grid refernce '%s' is valid for the specified spatial system".format(record.gridReferenceRaw.get)
        def reference = record.key
      }
    }

    def fail  = {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-159: Grid refernce '%s' does not match the spatial system".format(record.gridReferenceRaw.get)
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
            def message = "NBNV-159: Grid refernce type or srs is not recognised"
            def reference = record.key
          }
        }
      }
    }
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-159: Grid reference type not specified"
        def reference = record.key
      }
    }

  }


}
