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

  def validate(record: NbnRecord) = {
    val current = new Date()

    val resultList = new ListBuffer[Result]

    if (record.startDate.isDefined && record.startDate.get.after(current)) {
      val r1 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "Start date is in the future"
      }

      resultList.append(r1)
    }

    if (record.endDate.isDefined && record.endDate.get.after(current)) {
      val r2 = new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "End date is in the future"
      }

      resultList.append(r2)
    }

    resultList
  }




}
