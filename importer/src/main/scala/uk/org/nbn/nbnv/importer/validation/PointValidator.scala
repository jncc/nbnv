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

    //if valid srs
    if (r1.level == ResultLevel.DEBUG) {
      record.srs match {
        case Some(4326) => //todo - lat long valiation
        case _ => {

        }
      }
    }

    resultList.toList
  }
}
