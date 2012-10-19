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
      val latLongValidator = new LatLongValidator(db)
      val results = latLongValidator.validate(record)
      resultList.appendAll(results)
    }
    //else one of the supported grid ref systems
    else if (r1.level == ResultLevel.DEBUG) {
      val enValidator = new EastingNorthingValidator
      val results = enValidator.validate(record)
      resultList.appendAll(results)
    }

    resultList.toList
  }
}
