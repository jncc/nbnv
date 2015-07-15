package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv61ValidatorSuite extends BaseFunSuite{
  test("Nbnv61 should validate non-empty key") {
    val record = mock[NbnRecord]
    when(record.key).thenReturn("KEY1")

    val v = new Nbnv61Validator
    val r = v.processRecord(record)

    r.level should be (ResultLevel.DEBUG)
  }
 
  test("Nbnv61 should not validate duplicate key") {
    val record = mock[NbnRecord]
    when(record.key).thenReturn("KEY1")
	
	val record2 = mock[NbnRecord]
	when(record2.key).thenReturn("KEY1")

    val v = new Nbnv61Validator
    val r = v.processRecord(record)
	
    r.level should be (ResultLevel.DEBUG)
	
	val r2 = v.processRecord(record2)
	
	r2.level should be (ResultLevel.ERROR)
  }  

  test("Nbnv61 should not validate duplicate key with different cases") {
    val record = mock[NbnRecord]
    when(record.key).thenReturn("KEY1")
	
	val record2 = mock[NbnRecord]
	when(record2.key).thenReturn("key1")

    val v = new Nbnv61Validator
    val r = v.processRecord(record)
	
    r.level should be (ResultLevel.DEBUG)
	
	val r2 = v.processRecord(record2)
	
	r2.level should be (ResultLevel.ERROR)
  }    
  
}
