package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import java.util.Date
import java.text.SimpleDateFormat
import collection.mutable.ListBuffer

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 */
//todo: test this
class Nbnv71Validator {
  val code = "NBNV-71"

  def validate(record: NbnRecord) = {
    val current = new Date()

    val resultList = new ListBuffer[Result]

    if (record.startDate.isDefined && record.startDate.get.after(current)) {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: The StartDate must not be in the future".format(code)
      }

    } else {
      new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "%s1:The start date is not in the future".format(code)
      }
    }
  }
}
