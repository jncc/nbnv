package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import collection.mutable.ListBuffer

class Nbnv84Validator {
  def validate(record: NbnRecord) = {
    val code = "NBNV-84"

    val results = new ListBuffer[Result]

    if (!(record.eastRaw.isDefined && record.eastRaw.get.maybeDouble.isDefined)) {
      results.append( new Result {
        def level = ResultLevel.ERROR
        def message = "%s: The value of east is not a valid numeric value".format(code)
        def reference = record.key
      })
    }
    else  {
      results.append( new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: east is numeric".format(code)
        def reference = record.key
      })
    }

    if (!(record.northRaw.isDefined && record.northRaw.get.maybeDouble.isDefined)) {
      results.append( new Result {
        def level = ResultLevel.ERROR
        def message = "%s: The value of north is not a valid numeric value".format(code)
        def reference = record.key
      })
    }
    else  {
      results.append( new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: north is numeric".format(code)
        def reference = record.key
      })
    }

    results.toList
  }
}
