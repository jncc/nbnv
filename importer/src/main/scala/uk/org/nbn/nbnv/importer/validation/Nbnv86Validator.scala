package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import collection.mutable.ListBuffer

class Nbnv86Validator {
  def validate(record: NbnRecord) = {
    val longitude = record.east.get
    val latitude = record.north.get

    val code = "NBNV-86"

    val results = new ListBuffer[Result]

    if (longitude > 13 || longitude < -14) {
      results.append(new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: Longitude is outside the range of -14 to 13 used by the NBN Gateway".format(code)
      })
    }
    else{
      results.append(new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "%s: Validated: Longitude is in range".format(code)
      })
    }


    if (latitude > 62 || latitude < 48) {
     results.append( new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.ERROR
        def reference: String = record.key
        def message: String = "%s: Latitude is outside the range of -48 to 62 used by the NBN Gateway".format(code)
      })
    }
    else{
      results.append( new Result {
        def level: ResultLevel.ResultLevel = ResultLevel.DEBUG
        def reference: String = record.key
        def message: String = "%s: Latitude is in range".format(code)
      })
    }

    results.toList
  }
}
