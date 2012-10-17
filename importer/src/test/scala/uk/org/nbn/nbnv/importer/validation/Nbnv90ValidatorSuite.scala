package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.spatial.{GridSquareInfo, GridSquareInfoFactory}
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import org.mockito.Matchers._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv90ValidatorSuite extends BaseFunSuite {

  val knownGridRef_100m = "NN166712"
  val knownGridRef_1000m = "NN1671"

  test("should validate 100m grid ref reduced to precision of 1000m") {
    val gsi = mock[GridSquareInfo]
    when(gsi.gridReferencePrecision).thenReturn(100)

    val factory = mock[GridSquareInfoFactory]
    when(factory.getByGridRef(knownGridRef_100m)).thenReturn(gsi)

    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(Some(knownGridRef_100m))
    when(record.gridReferencePrecision).thenReturn(1000)

    val v = new Nbnv90Validator(factory)
    val results = v.validate(record)
    val result = results.find(r => r.level == ResultLevel.ERROR)

    result should be (None)
  }

  test("should validate if no target precision is requested") {
    val gsi = mock[GridSquareInfo]
    when(gsi.gridReferencePrecision).thenReturn(100)

    val factory = mock[GridSquareInfoFactory]
    when(factory.getByGridRef(knownGridRef_100m)).thenReturn(gsi)

    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(Some(knownGridRef_100m))
    when(record.gridReferencePrecision).thenReturn(0)

    val v = new Nbnv90Validator(factory)
    val results = v.validate(record)
    val result = results.find(r => r.level == ResultLevel.ERROR)

    result should be (None)
  }

  test("should not validate 1000m grid ref increased to 100m") {
    val gsi = mock[GridSquareInfo]
    when(gsi.gridReferencePrecision).thenReturn(1000)

    val factory = mock[GridSquareInfoFactory]
    when(factory.getByGridRef(knownGridRef_1000m)).thenReturn(gsi)

    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(Some(knownGridRef_1000m))
    when(record.gridReferencePrecision).thenReturn(100)

    val v = new Nbnv90Validator(factory)
    val results = v.validate(record)
    val result = results.find(r => r.level == ResultLevel.ERROR)

    result should not be (None)
  }

  test("should warn but not error if target precision is less then 100 and grid ref precision is 100") {
    val gsi = mock[GridSquareInfo]
    when(gsi.gridReferencePrecision).thenReturn(100)

    val factory = mock[GridSquareInfoFactory]
    when(factory.getByGridRef(knownGridRef_100m)).thenReturn(gsi)

    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(Some(knownGridRef_100m))
    when(record.gridReferencePrecision).thenReturn(50)

    val v = new Nbnv90Validator(factory)
    val results = v.validate(record)

    val error = results.find(r => r.level == ResultLevel.ERROR)
    error should be (None)

    val warn = results.find(r => r.level == ResultLevel.WARN)
    warn should not be (None)
  }

  test("should warn and error if target precision is less then 100 and grid ref precision is greater than 100") {
    val gsi = mock[GridSquareInfo]
    when(gsi.gridReferencePrecision).thenReturn(1000)

    val factory = mock[GridSquareInfoFactory]
    when(factory.getByGridRef(knownGridRef_1000m)).thenReturn(gsi)

    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(Some(knownGridRef_1000m))
    when(record.gridReferencePrecision).thenReturn(50)

    val v = new Nbnv90Validator(factory)
    val results = v.validate(record)

    val error = results.find(r => r.level == ResultLevel.ERROR)
    error should not be (None)

    val warn = results.find(r => r.level == ResultLevel.WARN)
    warn should not be (None)
  }

  test("should error if target precision is greater then 10,000m") {
    val factory = mock[GridSquareInfoFactory]
    val record = mock[NbnRecord]
    when(record.gridReference).thenReturn(Some(knownGridRef_1000m))
    when(record.gridReferencePrecision).thenReturn(10001)

    val v = new Nbnv90Validator(factory)
    val results = v.validate(record)

    val error = results.find(r => r.level == ResultLevel.ERROR)
    error should not be (None)

  }
}
