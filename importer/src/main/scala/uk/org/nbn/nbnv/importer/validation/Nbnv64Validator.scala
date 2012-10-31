package uk.org.nbn.nbnv.importer.validation

import com.google.inject.Inject

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

import uk.org.nbn.nbnv.importer.data.Repository

class Nbnv64Validator (repo: Repository) {
  def validate(record: NbnRecord) = {

    if (repo.confirmTaxonVersionKey(record.taxonVersionKey)) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: TaxonVersionKey '%s' exists.".format(record.taxonVersionKey)
        def reference = record.key
      }
    } else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "TaxonVersionKey '%s' must exist.".format(record.taxonVersionKey)
        def reference = record.key
      }
    }
  }
}
