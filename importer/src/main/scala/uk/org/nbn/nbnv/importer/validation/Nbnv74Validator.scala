package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import java.util.Calendar
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 *
 * Check for a valid O / OO vague date type
 */
class Nbnv74Validator {
  val start = Calendar.getInstance()
  val end = Calendar.getInstance()


  def validate(record: NbnRecord) = record.dateType match {
    case "O" => validateO(record: NbnRecord)
    case "OO" => validateOO(record: NbnRecord)
    case _ => failNonType(record) // Should never get here
  }

  def validateO(record: NbnRecord) : Result = {
    start.setTime(record.startDate)
    end.setTime(record.endDate)

    if ((start.get(Calendar.MONTH) == end.get(Calendar.MONTH)) && (start.get(Calendar.YEAR) == end.get(Calendar.YEAR))) {
      validateOO(record)
    } else {
      fail(record)
    }
  }

  def validateOO(record:NbnRecord) : Result = {
    start.setTime(record.startDate)
    end.setTime(record.endDate)

    if (start.get(Calendar.DAY_OF_MONTH) != start.getActualMinimum(Calendar.DAY_OF_MONTH)) {
      return fail(record)
    }
    if (end.get(Calendar.DAY_OF_MONTH) != end.getActualMaximum(Calendar.DAY_OF_MONTH)) {
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
      def message: String = "Found an invalid set of dates for a '%s' record [start: %s end: %s]".format(record.dateType, record.startDateRaw, record.endDateRaw)
    }
  }

  def failNonType(record: NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.ERROR
      def reference: String = record.dateType
      def message: String = "This validator only accepts types 'O' and 'OO', found '%s'".format(record.dateType)
    }
  }
}
