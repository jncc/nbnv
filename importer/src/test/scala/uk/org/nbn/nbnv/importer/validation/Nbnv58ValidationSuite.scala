package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv58ValidationSuite extends BaseFunSuite {
  test("Nbnv58 should validate if a grid reference is specified") {
    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(Some("AB12345"))

    val v = new Nbnv58Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv58 should validate if an easting, northing and srs is specified") {
    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(None)
    when(record.east).thenReturn(Some(1234))
    when(record.north).thenReturn(Some(1234))
    when(record.srs).thenReturn(Some("27700"))

    val v = new Nbnv58Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv58 should validate if an NBN feature key is specified") {
    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(None)
    when(record.east).thenReturn(None)
    when(record.north).thenReturn(None)
    when(record.srs).thenReturn(None)
    when(record.featureKey).thenReturn(Some("11111"))

    val v = new Nbnv58Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nbnv58 should not validate if an no location columns are identified") {
    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(None)
    when(record.east).thenReturn(None)
    when(record.north).thenReturn(None)
    when(record.srs).thenReturn(None)
    when(record.featureKey).thenReturn(None)

    val v = new Nbnv58Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
