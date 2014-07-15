package uk.org.nbn.nbnv.importer.validation

import com.google.inject.Inject

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

import uk.org.nbn.nbnv.importer.data.Repository

//validate taxon version key exists
class Nbnv64Validator (repo: Repository) {
  def validate(record: NbnRecord) = {

    val code = "NBNV-64"

    if (!record.taxonVersionKey.isDefined) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: A taxon version key must be provided.".format(code)
        def reference = record.key
      }
    } else
    if (repo.confirmTaxonVersionKey(record.taxonVersionKey.get)) {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: TaxonVersionKey '%s' exists.".format(code, record.taxonVersionKey.get)
        def reference = record.key
      }
    } else {
      new Result {
        def level = ResultLevel.ERROR
        def message = "%s: TaxonVersionKey '%s' does not exist.".format(code, record.taxonVersionKey.get)
        def reference = record.key
      }
    }
  }
}
