package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.data.Database

class SpatialReferenceValidator (db: Database) {

  def validate(record: NbnRecord) = {
    val resultList = new ListBuffer[Result]

    // spatial reference supplied
    val v1 = new Nbnv82Validator
    val r1 = v1.validate(record)
    resultList.append(r1)

    //Check spatial reference in more detail if no errors so far
    if (resultList.find(r => r.level == ResultLevel.ERROR) == None){
      if (record.gridReferenceRaw.isDefined) {
        //validate a grid reference
        val grv = new GridReferenceValidator(db)
        resultList.appendAll(grv.validate(record))
      }
      else if (record.featureKey.isDefined) {
        //check that a feature key is vlaid
        val v3 = new Nbnv88Validator(db)
        val r3 = v3.validate(record)
        resultList.append(r3)
      }
      else if (record.eastRaw.isDefined && record.northRaw.isDefined) {
        //validate a point
        val pv = new PointValidator
        resultList.appendAll(pv.validate(record))
      }
    }

    resultList.toList
  }
}
