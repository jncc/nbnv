package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import collection.mutable.ListBuffer

class ObservationAttributeValidator {
  def validate(record: NbnRecord) = {
    val resultList = new ListBuffer[Result]

    //validate that the attributes are properly formed json
    val v0 = new Nbnv878Validator
    resultList.append(v0.validate(record))

    //validate each attribute in the list
    if (resultList.find(r => r.level == ResultLevel.ERROR) == None) {
      for (attribute <- record.attributes) {
        //validate attribute value
        val v1 = new Nbnv93Validator
        resultList.append(v1.validate(attribute, record.key))

        //validate attribute label
        val v2 = new Nbnv115Validator
        resultList.append(v2.validate(attribute, record.key))
      }
    }

    resultList.toList
  }
}
