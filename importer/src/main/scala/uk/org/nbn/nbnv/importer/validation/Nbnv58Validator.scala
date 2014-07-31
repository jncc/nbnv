package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.archive.ArchiveMetadata

//validate a location fields have been defined
class Nbnv58Validator {
  def validate(metadata: ArchiveMetadata) = {
    val code = "NBNV-58"

    if ((metadata.gridReference.isDefined)
      || (metadata.east.isDefined && metadata.north.isDefined && (metadata.srs.isDefined || metadata.gridReferenceType.isDefined))
      || metadata.featureKey.isDefined) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: An acceptable combination of location fields has been specified.".format(code)
        def reference = "Location Field Mapping"
      }
    } else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: One or more of the spatial reference fields specified in the NBN Exchange Format are missing".format(code)
        def reference = "Location Field Mapping"
      }
    }
  }
}
