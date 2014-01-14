package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.archive.ArchiveMetadata

//validate a location fields have been defined
class Nbnv58Validator {
  def validate(metadata: ArchiveMetadata) = {
    if ((metadata.gridReference.isDefined)
      || (metadata.east.isDefined && metadata.north.isDefined && (metadata.srs.isDefined || metadata.gridReferenceType.isDefined))
      || metadata.featureKey.isDefined) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-58: Validated: An acceptable combination of location fields has been specified."
        def reference = "Location Field Mapping"
      }
    } else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-58: A record must have a location defined for it. An acceptable combination of location fields has not been specified."
        def reference = "Location Field Mapping"
      }
    }
  }
}
