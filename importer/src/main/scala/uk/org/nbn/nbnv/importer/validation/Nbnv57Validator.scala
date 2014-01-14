package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.archive.ArchiveMetadata

//validate dates fields have been defined
class Nbnv57Validator {
  def validate(metadata: ArchiveMetadata) = {
    if ((metadata.date.isDefined)
    || (metadata.startDate.isDefined && metadata.dateType.isDefined)
    || (metadata.endDate.isDefined && metadata.dateType.isDefined)) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "NBNV-57: Validated: A record date field has been defined."
        def reference = "Date Field Mapping"
      }
    } else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "NBNV-57: A record date field must be defined. Either a darwin core eventDate field or an NBN eventDate field must be specified. The date type must also be specified"
        def reference = "Date Field Mapping"
      }
    }
  }
}

