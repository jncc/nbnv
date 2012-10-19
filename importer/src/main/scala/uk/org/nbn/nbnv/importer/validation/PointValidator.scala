package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.data.Database
import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class PointValidator(db: Database) {
  val resultList = new ListBuffer[Result]

  def validate(record: NbnRecord) = {
    //check the spatial reference system is valid
    val v1 = new Nbnv185Validator
    val r1 = v1.validate(record)
    resultList.append(r1)

    //if WGS84 Lat Long
    if (r1.level == ResultLevel.DEBUG && record.srs.get == 4326) {
    }
    //else one of the supported grid ref systems
    else if (r1.level == ResultLevel.DEBUG) {
      //test easting and northing are numeric.
      val v3 = new Nbnv85Validator
      val r3 = v3.validate(record)
      resultList.append(r3)

      if (r3.level == ResultLevel.DEBUG) {
        //test easting and northing are valid for the specified srs
        val v4 = new Nbnv87Validator
        val r4 = v4.validate(record)
        resultList.append(r4)
      }

    }

    resultList.toList
  }
}
