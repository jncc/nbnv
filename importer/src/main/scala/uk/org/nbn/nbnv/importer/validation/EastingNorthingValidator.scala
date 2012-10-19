package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}
import collection.mutable.ListBuffer

class EastingNorthingValidator {
  def validate(record: NbnRecord) = {
    val resultList = new ListBuffer[Result]

    //test easting and northing are numeric.
    val v1 = new Nbnv85Validator
    val r1 = v1.validate(record)
    resultList.append(r1)

    if (r1.level == ResultLevel.DEBUG) {
      //test easting and northing are valid for the specified srs
      val v2 = new Nbnv87Validator
      val r2 = v2.validate(record)
      resultList.append(r2)
    }

    resultList.toList
  }
}
