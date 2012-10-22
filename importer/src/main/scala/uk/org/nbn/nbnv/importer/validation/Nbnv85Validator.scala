package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.data.Database
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.spatial.GridSquareInfoFactory
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

//validate that the easting and northing a
class Nbnv85Validator() {
  def validate(record: NbnRecord) = {

    if ((record.eastRaw.get matches """^\d+(\.\d+)*$""") == false) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "The value of east is not a numeric easting: %s".format(record.eastRaw.get)
        def reference = "Record " + record.key
      }
    }
    else if ((record.northRaw.get matches """^\d+(\.\d+)*$""") == false) {
      new Result {
        def level = ResultLevel.ERROR
        def message = "The value of north is not a numeric northing: %s".format(record.northRaw.get)
        def reference = "Record " + record.key
      }
    }
    else  {
      new Result {
        def level = ResultLevel.DEBUG
        def message = "Validated: East and North are numeric"
        def reference = "Record " + record.key
      }
    }

  }
}
