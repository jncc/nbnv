package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import scala.Some
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv84ValidatorSuite extends BaseFunSuite {
  test("should validate a positive latitude and longitude") {
    val rec = mock[NbnRecord]
    when(rec.eastRaw).thenReturn(Some("123.23"))
    when(rec.northRaw).thenReturn(Some("12.23"))

    val v = new Nbnv84Validator
    val r = v.validate(rec)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)

  }

  test("should validate a negative latitude and longitude") {
    val rec = mock[NbnRecord]
    when(rec.eastRaw).thenReturn(Some("-123.23"))
    when(rec.northRaw).thenReturn(Some("-12.23"))

    val v = new Nbnv84Validator
    val r = v.validate(rec)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }


  test("should not validate a non numeric longitude") {
    val rec = mock[NbnRecord]
    when(rec.eastRaw).thenReturn(Some("abc"))
    when(rec.northRaw).thenReturn(Some("-12.23"))

    val v = new Nbnv84Validator
    val r = v.validate(rec)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate a non numeric latitude") {
    val rec = mock[NbnRecord]
    when(rec.eastRaw).thenReturn(Some("123.33"))
    when(rec.northRaw).thenReturn(Some("abc"))

    val v = new Nbnv84Validator
    val r = v.validate(rec)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }
}
