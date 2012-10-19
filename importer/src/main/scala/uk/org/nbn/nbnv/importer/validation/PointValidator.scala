package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.data.Database
import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.Result

class PointValidator(db: Database) {
  val resultList = new ListBuffer[Result]

  def validate(record: NbnRecord) = {
    //check the spatial reference system is valid
    val v1 = new Nbnv185Validator
    val r1 = v1.validate(record)
    resultList.append(r1)

    resultList.toList
  }
}
