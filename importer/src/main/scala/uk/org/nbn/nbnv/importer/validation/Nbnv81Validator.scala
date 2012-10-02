package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.spatial.GridRefPatterns
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import scala.util.matching.Regex

class Nbnv81Validator {
  def validate(gridReference: String, recordKey: String) = {

    def success = {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: A valid grid reference was supplied"
        def reference = "Record " + recordKey
      }
    }

    if (gridReference.matches(GridRefPatterns.ukGridRef)) success
    else if (gridReference.matches(GridRefPatterns.ukDintyGridRef)) success
    else if (gridReference.matches(GridRefPatterns.irishGridRef)) success
    else if (gridReference.matches(GridRefPatterns.irishDintyGrid)) success
    else if (gridReference.matches(GridRefPatterns.channelIslandsGridRef)) success
    else if (gridReference.matches(GridRefPatterns.channelIslandsDintyGridRef)) success
    else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "'%s' is not a valid grid reference".format(gridReference)
        def reference = "Record " + recordKey
      }
    }
  }
}
