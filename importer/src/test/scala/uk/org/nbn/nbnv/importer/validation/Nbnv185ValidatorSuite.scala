package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv185ValidatorSuite extends BaseFunSuite {

  test("should validate srs 27700") {
    val rec = mock[NbnRecord]
    when(rec.srsRaw).thenReturn(Option("27700"))

    val v = new Nbnv185Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate srs 29903") {
    val rec = mock[NbnRecord]
    when(rec.srsRaw).thenReturn(Option("29903"))

    val v = new Nbnv185Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate srs 23030") {
    val rec = mock[NbnRecord]
    when(rec.srsRaw).thenReturn(Option("23030"))

    val v = new Nbnv185Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate srs 4326") {
    val rec = mock[NbnRecord]
    when(rec.srsRaw).thenReturn(Option("4326"))

    val v = new Nbnv185Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate unrecognised srs") {
    val rec = mock[NbnRecord]
    when(rec.srsRaw).thenReturn(Option("11111"))

    val v = new Nbnv185Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate a blank srs and grid ref type") {
    val rec = mock[NbnRecord]
    when(rec.srsRaw).thenReturn(None)
    when(rec.gridReferenceTypeRaw).thenReturn(None)

    val v = new Nbnv185Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.ERROR)
  }

  //todo add tests for grid ref type too
}
