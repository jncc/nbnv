package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.data.Database
import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class PointValidator {
  val resultList = new ListBuffer[Result]

  def validate(record: NbnRecord) = {
    //check the spatial reference system is valid
    val v1 = new Nbnv185Validator
    val r1 = v1.validate(record)
    resultList.append(r1)


    //If no errors so far test point validity
    if (resultList.find(r => r.level == ResultLevel.ERROR) == None) {
      //if WGS84 Lat Long
      if (record.srs.get == 4326) {
        //check the lat long are numeric values
        val v2 = new Nbnv84Validator
        val r2 = v2.validate(record)
        resultList.append(r2)

        //check lat long falls with in supported range of values
        val v3 = new Nbnv86Validator
        val r3 = v3.validate(record)
        resultList.append(r3)
      }
      //else one of the supported grid ref systems
      else {
        //check the values of east and north are positive numeric values
        val v4 = new Nbnv85Validator
        val r4 = v4.validate(record)
        resultList.append(r4)

        //check that easting northing fall within specified grid system
        val v5 = new Nbnv87Validator
        val r5 = v5.validate(record)
        resultList.append(r5)
      }
    }

    resultList.toList
  }
}
