package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import collection.mutable.ListBuffer

//validate that the easting and northing a
class Nbnv85Validator() {
  def validate(record: NbnRecord) = {

    val results = new ListBuffer[Result]
    val code = "NBNV-85"

    if (!(record.eastRaw.isDefined && record.eastRaw.get.maybeDouble.isDefined && record.eastRaw.get.maybeDouble.get > 0 )) {
      results.append(new Result {
        def level = ResultLevel.ERROR
        def message = "%s: Easting must be a positive number".format(code)
        def reference = record.key
      })
    }
    else  {
      results.append(new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: East and North are numeric".format(code)
        def reference = record.key
      })
    }

    if (!(record.northRaw.isDefined && record.northRaw.get.maybeDouble.isDefined && record.northRaw.get.maybeDouble.get > 0)) {
      results.append( new Result {
        def level = ResultLevel.ERROR
        def message = "%s: Northing must be positive number".format(code)
        def reference = record.key
      })
    }
    else  {
      results.append( new Result {
        def level = ResultLevel.DEBUG
        def message = "%s: Validated: East and North are numeric".format(code)
        def reference = record.key
      })
    }

    results.toList
  }
}
