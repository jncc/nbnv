package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv85ValidatiorSuite extends BaseFunSuite {

  test("should validate an integer easting and norhting"){
    val rec = mock[NbnRecord]
    when(rec.eastRaw).thenReturn(Some("123"))
    when(rec.northRaw).thenReturn(Some("123"))

    val v = new Nbnv85Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate a decimal easting and northing"){
    val rec = mock[NbnRecord]
    when(rec.eastRaw).thenReturn(Some("123.23"))
    when(rec.northRaw).thenReturn(Some("123.23"))

    val v = new Nbnv85Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate negative values"){
    val rec = mock[NbnRecord]
    when(rec.eastRaw).thenReturn(Some("-1.123"))
    when(rec.northRaw).thenReturn(Some("-47.23"))

    val v = new Nbnv85Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.ERROR)
  }


  test("should not validate a non numeric easting"){
    val rec = mock[NbnRecord]
    when(rec.eastRaw).thenReturn(Some("123E"))
    when(rec.northRaw).thenReturn(Some("123"))

    val v = new Nbnv85Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate a non numeric norhing") {
    val rec = mock[NbnRecord]
    when(rec.eastRaw).thenReturn(Some("123"))
    when(rec.northRaw).thenReturn(Some("123E"))

    val v = new Nbnv85Validator
    val r = v.validate(rec)

    r.level should be (ResultLevel.ERROR)
  }

}
