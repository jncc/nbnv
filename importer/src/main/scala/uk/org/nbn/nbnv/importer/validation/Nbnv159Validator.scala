package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.spatial.{GridReferenceTypeMapper, GridRefPatterns}
import com.google.inject.Inject

class Nbnv159Validator (grtMapper: GridReferenceTypeMapper) {
  def validate(gridReference : String, gridReferenceType : Option[String], recordKey : String)  = {

    def success = {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Grid refernce '%s' matches type '%s'".format(gridReference, gridReferenceType.get)
        def reference = "Record " + recordKey
      }
    }

    def fail  = {
      new Result {
        def level = ResultLevel.ERROR
        def message = "Grid refernce '%s' does not match type '%s'".format(gridReference, gridReferenceType.get)
        def reference = "Record " + recordKey
      }
    }

    if(gridReferenceType.isDefined) {

      grtMapper.map(gridReferenceType.get) match {

        case Some("OSGB36") => {
          if (gridReference.matches(GridRefPatterns.ukGridRef)
          || gridReference.matches(GridRefPatterns.ukDintyGridRef))
          success else fail
        }
        case Some("OSNI") => {
          if (gridReference.matches(GridRefPatterns.irishGridRef)
            || gridReference.matches(GridRefPatterns.irishDintyGrid))
            success else fail
        }
        case Some("ED50") => {
          if (gridReference.matches(GridRefPatterns.channelIslandsGridRef)
            || gridReference.matches(GridRefPatterns.channelIslandsDintyGridRef))
            success else fail
        }
        case None => {
          new Result {
            def level = ResultLevel.ERROR
            def message = "Grid refernce type '%s' is not recognised".format(gridReferenceType.get)
            def reference = "Record " + recordKey
          }
        }
      }
    }
    else {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: Grid reference type not specified".format(gridReference, gridReferenceType)
        def reference = "Record " + recordKey
      }
    }

  }


}
