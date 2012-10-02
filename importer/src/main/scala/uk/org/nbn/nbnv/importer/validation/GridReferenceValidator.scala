package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.Result

class GridReferenceValidator {
  def validate(record: NbnRecord) = {
    val resultList = new ListBuffer[Result]

    val v1 = new Nbnv81Validator
    val r1 = v1.validate(record.gridReference.get, record.key)
    resultList.append(r1)

    resultList.toList
  }
}
