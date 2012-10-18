package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import java.text.SimpleDateFormat
import java.awt.CardLayout
import java.util.Calendar

/**
 * Created By: Matt Debont
 * Date: 18/10/12
 */
class Nbnv76Validator {

  def validate(record: NbnRecord): Result = record.dateType match {
    case "-Y" => validateMinusY(record)
    case _    => new Result {def level: ResultLevel.ResultLevel = ResultLevel.ERROR; def reference: String = record.dateType; def message: String = "This validator only supports -Y date types"}
  }

  def validateMinusY(record: NbnRecord): Result = {
    if (record.endDate != null) {
      val end = Calendar.getInstance()
      end.setTime(record.endDate)

      if (record.startDate == null && end.get(Calendar.MONTH) == end.getActualMaximum(Calendar.MONTH) && end.get(Calendar.DAY_OF_MONTH) == end.getActualMaximum(Calendar.DAY_OF_MONTH)) {
        new Result {
          def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
          def reference: String = record.dateType
          def message: String = "Found a valid date type and dates"
        }
      } else {
        fail(record)
      }
    }else {
      fail(record)
    }
  }

  def fail(record: NbnRecord) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.ERROR
      def reference: String = record.dateType
      def message: String = "Start date must be null and end date 31st Dec"
    }
  }
}
