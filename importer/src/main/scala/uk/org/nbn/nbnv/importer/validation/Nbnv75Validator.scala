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
      failY(record)
    }
  }

  def validateYY(record:NbnRecord): Result = {
    start.setTime(record.startDate)
    end.setTime(record.endDate)

    if (start.get(Calendar.DAY_OF_YEAR) != start.getActualMinimum(Calendar.DAY_OF_YEAR)) {
      return failYY(record)
    }
    if (end.get(Calendar.DAY_OF_YEAR) != end.getActualMaximum(Calendar.DAY_OF_YEAR)) {
      return failYY(record)
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

  def failY(record: NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.ERROR
      def reference: String = record.dateType
      def message: String = "Found an invalid set of dates for a 'Y' record [start: %s end: %s] dates should be at the start and end of the SAME year".format(record.startDateRaw, record.endDateRaw)
    }
  }

  def failYY(record: NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.ERROR
      def reference: String = record.dateType
      def message: String = "Found an invalid set of dates for a 'YY' record [start: %s end: %s] dates should be at the start and end of the years".format(record.startDateRaw, record.endDateRaw)
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
