package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.spatial._
import uk.org.nbn.nbnv.importer.spatial.BritishGrid
import uk.org.nbn.nbnv.importer.spatial.IrishGrid

class Nbnv159Validator () {
  def validate(gridReference : String, gridReferenceType : Option[String], recordKey : String)  = {

    def success = {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Grid refernce '%s' matches type '%s'".format(gridReference, gridReferenceType.get)
        def reference = recordKey
      }
    }

    def fail  = {
      new Result {
        def level = ResultLevel.ERROR
        def message = "Grid refernce '%s' does not match type '%s'".format(gridReference, gridReferenceType.get)
        def reference = recordKey
      }
    }

    if(gridReferenceType.isDefined) {

      GridSystem(gridReferenceType.get) match {

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
            def message = "Grid refernce type '%s' is not recognised".format(gridReferenceType.get)
            def reference = recordKey
          }
        }
      }
    }
    else {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Grid reference type not specified".format(gridReference, gridReferenceType)
        def reference = recordKey
      }
    }

  }


}
