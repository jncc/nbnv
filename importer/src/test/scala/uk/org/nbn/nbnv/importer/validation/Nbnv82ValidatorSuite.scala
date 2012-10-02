package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv82ValidatorSuite extends BaseFunSuite {

  test("Nbnv82 should validate if a grid ref is provided") {
    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(Some("NN166712"))
    when(record.featureKey).thenReturn(None)
    when(record.east).thenReturn(None)
    when(record.north).thenReturn(None)

    val v = new Nbnv82Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv82 should validate if an easting and northing are provided") {
    val record = mock[NbnRecord]
    when(record.east).thenReturn(Some("377562"))
    when(record.north).thenReturn(Some("6296480"))
    when(record.gridReference).thenReturn(None)
    when(record.featureKey).thenReturn(None)

    val v = new Nbnv82Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv82 should validate if a featureKey is provided") {
    val record = mock[NbnRecord]
    when(record.featureKey).thenReturn(Some("SB0003253"))
    when(record.east).thenReturn(None)
    when(record.north).thenReturn(None)
    when(record.gridReference).thenReturn(None)

    val v = new Nbnv82Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv82 should not validate if no feature identifier is provided") {
    val record = mock[NbnRecord]
    when(record.featureKey).thenReturn(None)
    when(record.east).thenReturn(None)
    when(record.north).thenReturn(None)
    when(record.gridReference).thenReturn(None)

    val v = new Nbnv82Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

}
