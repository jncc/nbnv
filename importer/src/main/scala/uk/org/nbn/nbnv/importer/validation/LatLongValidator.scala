package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.data.Database
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.Result
import collection.mutable.ListBuffer

class LatLongValidator(db: Database) {
  def validate(record: NbnRecord) = {
    val resultList = new ListBuffer[Result]
    //

    resultList.toList
  }
}
