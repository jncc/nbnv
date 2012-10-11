package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.records.NbnRecord
import collection.mutable.ListBuffer
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}
import com.google.inject.Inject
import uk.org.nbn.nbnv.importer.spatial.GridReferenceTypeMapper

class GridReferenceValidator  {
  def validate(record: NbnRecord) = {
    val resultList = new ListBuffer[Result]

    //is valid grid ref
    val v1 = new Nbnv81Validator
    val r1 = v1.validate(record.gridReference.get, record.key)
    resultList.append(r1)

    if(r1.level == ResultLevel.DEBUG)
    {
      //does grid ref match grid ref type if specified.
      val v2 = new Nbnv159Validator(new GridReferenceTypeMapper)
      val r2 = v2.validate(record.gridReference.get, record.gridReferenceType, record.key)
      resultList.append(r2)
    }

    resultList.toList
  }
}
