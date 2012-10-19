package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.data.Database
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}

class Nbnv88Validator(db: Database) {
  def validate(record: NbnRecord) = {
    val featureKey = record.featureKey.get

    //datset key is 8 char + at least 1 char for provider key
    if (featureKey.length < 9) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "FeatureKey '%s' is to short to be valid".format(featureKey)
        def reference = "Record " + record.key
      }
    }
    else {
      val siteDatasetKey = featureKey.substring(0,8)
      val siteProviderKey = featureKey.substring(8, featureKey.length)

      if (db.repo.confirmSiteBoundary(siteDatasetKey, siteProviderKey)) {
        new Result {
          def level = ResultLevel.DEBUG
          def message = "Validated: FeatureKey '%s' exists.".format(featureKey)
          def reference = "Record " + record.key
        }
      }
      else {
        new Result {
          def level = ResultLevel.ERROR
          def message = "FeatureKey '%s' is invalid".format(featureKey)
          def reference = "Record " + record.key
        }
      }
    }
  }
}
