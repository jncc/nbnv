package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import java.util.Date
import java.text.SimpleDateFormat

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 */
class Nbnv71Validator {

  def validate(record: NbnRecord): Result = {
    val current = new Date()
    if (record.startDate != null && record.startDate.after(current)) {
      return fail(new SimpleDateFormat("dd/MM/yyyy").format(record.startDate))
    }
    if (record.endDate != null && record.endDate.after(current)) {
      return fail(new SimpleDateFormat("dd/MM/yyyy").format(record.endDate))
    }
    if (record.eventDate != null && record.eventDate.after(current)) {
      return fail(new SimpleDateFormat("dd/MM/yyyy").format(record.eventDate))
    }
    success
  }

  def success = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
      def reference: String = "No_Future_Dates"
      def message: String = "Date is valid, not in the future"
    }
  }

  def fail(dateString: String) = {
    new Result {
      def level: ResultLevel.ResultLevel = ResultLevel.ERROR
      def reference: String = dateString
      def message: String = "Date is in the future"
    }
  }
}
