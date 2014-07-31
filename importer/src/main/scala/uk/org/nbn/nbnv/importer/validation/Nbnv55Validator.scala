package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.archive.ArchiveMetadata
import uk.org.nbn.nbnv.importer.fidelity.Result

//validate the key has been mapped
class Nbnv55Validator {
  def validate(metadata: ArchiveMetadata): Result = {
    val v = new FieldMappingValidator
    v.validate("NBNV-55", "RecordKey", metadata.key)
  }
}
