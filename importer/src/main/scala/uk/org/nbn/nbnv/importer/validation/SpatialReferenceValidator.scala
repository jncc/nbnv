package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import com.google.inject.Inject

class SpatialReferenceValidator @Inject()(grv: GridReferenceValidator) {
  def validate(record: NbnRecord) = {
    val resultList = new ListBuffer[Result]

    // spatial reference supplied
    val v1 = new Nbnv82Validator
    val r1 = v1.validate(record)
    resultList.append(r1)

    //Check spatial reference in more detail if no errors so far
    if (resultList.find(r => r.level == ResultLevel.ERROR) == None){
      if (record.gridReference.isDefined) {
        resultList.appendAll(grv.validate(record))
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
