package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.spatial.GridRefPatterns
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import scala.util.matching.Regex

class Nbnv81Validator {
  val code = "NBNV-81"

  def validate(gridReference: String, recordKey: String) = {
    def success = {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: A valid grid reference was supplied".format(code)
        def reference = recordKey
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
        def message = "%s: The GridReference, %s is not a valid grid reference within the NBN Exchange Format".format(code, gridReference)
        def reference = recordKey
      }
    }
  }
}
