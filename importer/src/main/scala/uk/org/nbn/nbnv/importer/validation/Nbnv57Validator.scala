package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.archive.ArchiveMetadata

//validate dates fields have been defined
class Nbnv57Validator {
  def validate(metadata: ArchiveMetadata) = {
    val code = "NBNV-57"

    if ((metadata.date.isDefined)
    || (metadata.startDate.isDefined && metadata.dateType.isDefined)
    || (metadata.endDate.isDefined && metadata.dateType.isDefined)) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: A record date field has been defined.".format(code)
        def reference = "Date Field Mapping"
      }
    } else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: One or more of the date fields specified in the NBN Exchange Format are missing".format(code)
        def reference = "Date Field Mapping"
      }
    }
  }
}

