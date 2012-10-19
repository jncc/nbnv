package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv82ValidatorSuite extends BaseFunSuite {

  test("Nbnv82 should validate if a grid ref is provided") {
    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(Some("NN166712"))
    when(record.srsRaw).thenReturn(None)
    when(record.featureKey).thenReturn(None)
    when(record.eastRaw).thenReturn(None)
    when(record.northRaw).thenReturn(None)

    val v = new Nbnv82Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv82 should validate if an easting and northing are provided") {
    val record = mock[NbnRecord]

    when(record.eastRaw).thenReturn(Some("377562"))
    when(record.northRaw).thenReturn(Some("6296480"))
    when(record.srsRaw).thenReturn(Some("27700"))
    when(record.gridReference).thenReturn(None)
    when(record.featureKey).thenReturn(None)

    val v = new Nbnv82Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv82 should validate if a featureKey is provided") {
    val record = mock[NbnRecord]
    when(record.featureKey).thenReturn(Some("SB0003253"))
    when(record.srsRaw).thenReturn(None)
    when(record.eastRaw).thenReturn(None)
    when(record.northRaw).thenReturn(None)
    when(record.gridReference).thenReturn(None)

    val v = new Nbnv82Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv82 should not validate if no feature identifier is provided") {
    val record = mock[NbnRecord]
    when(record.featureKey).thenReturn(None)
    when(record.srsRaw).thenReturn(None)
    when(record.eastRaw).thenReturn(None)
    when(record.northRaw).thenReturn(None)
    when(record.gridReference).thenReturn(None)

    val v = new Nbnv82Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("Nbnv82 should not validate if a feature key and grid ref are supplied") {
    val record = mock[NbnRecord]
    when(record.featureKey).thenReturn(Some("SB0003253"))
    when(record.srsRaw).thenReturn(None)
    when(record.eastRaw).thenReturn(None)
    when(record.northRaw).thenReturn(None)
    when(record.gridReference).thenReturn(Some("NN166712"))

    val v = new Nbnv82Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("Nbnv82 should not validate if a feature key and easting northing are supplied ") {
    val record = mock[NbnRecord]
    when(record.featureKey).thenReturn(Some("SB0003253"))
    when(record.eastRaw).thenReturn(Some("377562"))
    when(record.northRaw).thenReturn(Some("629648"))
    when(record.srsRaw).thenReturn(Some("27700"))
    when(record.gridReference).thenReturn(None)

    val v = new Nbnv82Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("Nbnv82 should not validate if an easting northing and grid reference are supplied ") {
    val record = mock[NbnRecord]
    when(record.featureKey).thenReturn(None)
    when(record.eastRaw).thenReturn(Some("37756"))
    when(record.northRaw).thenReturn(Some("6296480"))
    when(record.srsRaw).thenReturn(Some("27700"))
    when(record.gridReference).thenReturn(Some("NN166712"))

    val v = new Nbnv82Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
