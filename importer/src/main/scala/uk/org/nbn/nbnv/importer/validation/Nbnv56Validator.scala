package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.archive.ArchiveMetadata

class Nbnv56Validator {
  def validate(metadata: ArchiveMetadata) = {
    val validator = new FieldMappingValidator
    validator.validate("NBNV-56", "TaxonVersionKey", metadata.taxonVersionKey)
  }
}
