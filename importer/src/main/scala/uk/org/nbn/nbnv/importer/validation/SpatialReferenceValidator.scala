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
      if (record.gridReference.isDefined) {
        val grv = new GridReferenceValidator(db)
        resultList.appendAll(grv.validate(record))
      }
      else if (record.featureKey.isDefined) {
        val v3 = new Nbnv88Validator(db)
        val r3 = v3.validate(record)
        resultList.append(r3)
      }
      else if (record.eastRaw.isDefined && record.northRaw.isDefined) {
        val pv = new PointValidator(db)
        resultList.appendAll(pv.validate(record))
      }
    }

    resultList.toList
  }
}
