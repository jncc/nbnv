package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import java.util.Calendar

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 *
 * Should correct vague date for type 'Y' or 'YY'
 */
class Nbnv75Validator {
  val start = Calendar.getInstance()
  val end = Calendar.getInstance()

  def validate(record: NbnRecord) = record.dateType match {
    case "Y"  => validateY(record)
    case "YY" => validateYY(record)
    case _    => failNonType(record)
  }

  def validateY(record:NbnRecord) = {
    start.setTime(record.startDate)
    end.setTime(record.endDate)

    if (start.get(Calendar.YEAR) == end.get(Calendar.YEAR)) {
      validateYY(record)
    } else {
      fail(record)
    }
  }

  def validateYY(record:NbnRecord): Result = {
    start.setTime(record.startDate)
    end.setTime(record.endDate)

    if (start.get(Calendar.DAY_OF_YEAR) != start.getActualMinimum(Calendar.DAY_OF_YEAR)) {
      return fail(record)
    }
    if (end.get(Calendar.DAY_OF_YEAR) != end.getActualMaximum(Calendar.DAY_OF_YEAR)) {
      return fail(record)
    }

    success(record)
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
      def message: String = "Start date must be 1st of Jan and end date 31st Dec"
    }
  }

  def failNonType(record: NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.ERROR
      def reference: String = record.dateType
      def message: String = "This validator only accepts types 'Y' and 'YY', found '%s'".format(record.dateType)
    }
  }
}
