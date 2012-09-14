package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv58ValidationSuite extends BaseFunSuite {
  test("Nbnv58 should validate if a grid reference is specified") {
    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn("AB12345")

    val v = new Nbnv58Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv58 should validate if an easting, northing and srs is specified") {
    val record = mock[NbnRecord]
    when(record.east).thenReturn("1234")
    when(record.north).thenReturn("1234")
    when(record.srs).thenReturn("27700")

    val v = new Nbnv58Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv58 should validate if an NBN feature key is specified") {
    val record = mock[NbnRecord]
    when(record.featureKey).thenReturn("11111")

    val v = new Nbnv58Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv58 should not validate if an no location columns are identified") {
    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(null)
    when(record.east).thenReturn(null)
    when(record.north).thenReturn(null)
    when(record.srs).thenReturn(null)
    when(record.featureKey).thenReturn(null)

    val v = new Nbnv58Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
