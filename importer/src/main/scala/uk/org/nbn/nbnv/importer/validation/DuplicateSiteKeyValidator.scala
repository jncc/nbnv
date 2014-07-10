package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import scala.collection.mutable.Map

/**
 * Created by felix mason on 30/06/2014.
 */
class DuplicateSiteKeyValidator {
  val code = "Duplicate Site Key"
  val siteKeyMap = Map[String, String] ()

  def processRecord(record: NbnRecord): Result = {
    if (siteKeyMap.contains(record.siteKey.get)) {
      if(record.siteName.getOrElse(record.siteKey.get) != siteKeyMap(record.siteKey.get)) {
        new Result {
          def level = ResultLevel.ERROR
          def message = "%s: The site key '%s' is already defined for a site called '%s'".format(code, record.siteKey.get, siteKeyMap(record.siteKey.get))
          def reference = record.key
        }
      } else {
        new Result {
          def level = ResultLevel.DEBUG
          def message = "%s: The site key is not duplicated".format(code)
          def reference = record.key
        }
      }
    } else {
      siteKeyMap(record.siteKey.get) = record.siteName.getOrElse(record.siteKey.get)

      new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: The site key is not duplicated".format(code)
        def reference = record.key
      }
    }
  }
}
