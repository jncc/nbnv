package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 *
 * Check values for vague date type 'D'
 */
class Nbnv73Validator {

  def validate(record: NbnRecord) = {
    if (record.dateType == "D") {
      if (record.startDate.equals(record.endDate)) success(record) else fail(record)
    } else failNonType(record)
  }

  def success(record: NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
      def reference: String = record.dateType
      def message: String = "Found an accepted DateType and associated dates"
    }
  }

  def fail(record: NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.ERROR
      def reference: String = record.dateType
      def message: String = "Start and end dates are not the same"
    }
  }

  def failNonType(record: NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.ERROR
      def reference: String = record.dateType
      def message: String = "This validator only accepts type 'D', found '%s'".format(record.dateType)
    }
  }
}
