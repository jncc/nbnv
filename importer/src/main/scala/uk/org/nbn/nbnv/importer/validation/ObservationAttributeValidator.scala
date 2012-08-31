package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.Result
import collection.mutable.ListBuffer

class ObservationAttributeValidator {
  def validate(record: NbnRecord) = {
    val resultList = new ListBuffer[Result]()

    for (attribute <- record.attributes) {
      val v0 = new Nbnv93Validator
      resultList.append(v0.validate(attribute, record.key))
    }

    resultList.toList
  }
}
