package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class SpatialReferenceValidator {
  def validate(record: NbnRecord) = {
    val resultList = new ListBuffer[Result]

    val v1 = new Nbnv82Validator
    val r1 = v1.validate(record)
    resultList.append(r1)

    if (r1.level == ResultLevel.DEBUG){
      if (record.gridReference.isDefined) {

      }
      else if (record.featureKey.isDefined) {
        //todo: validate feature key
      }
      else if (record.east.isDefined && record.north.isDefined) {
        //todo: validate point ?
      }
    }

    resultList.toList
  }
}
