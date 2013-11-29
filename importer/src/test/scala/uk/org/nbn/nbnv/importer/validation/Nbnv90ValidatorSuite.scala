package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.spatial.{GridSquareInfo, GridSquareInfoFactory}
import uk.org.nbn.nbnv.importer.records.{GridRefDef, NbnRecord}
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
    when(factory.getByGridRef(any[GridRefDef])).thenReturn(gsi)

    val record = mock[NbnRecord]
    when(record.gridReferenceRaw).thenReturn(Some(knownGridRef_100m))
    when(record.gridReferencePrecision).thenReturn(Some(1000))

    val v = new Nbnv90Validator(factory)
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate if a precision is not specified") {
    val gsi = mock[GridSquareInfo]
    when(gsi.gridReferencePrecision).thenReturn(100)

    val factory = mock[GridSquareInfoFactory]
    when(factory.getByGridRef(any[GridRefDef])).thenReturn(gsi)

    val record = mock[NbnRecord]
    when(record.gridReferenceRaw).thenReturn(Some(knownGridRef_100m))
    when(record.gridReferencePrecision).thenReturn(None)

    val v = new Nbnv90Validator(factory)
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate 1000m grid ref increased to 100m") {
    val gsi = mock[GridSquareInfo]
    when(gsi.gridReferencePrecision).thenReturn(1000)

    val factory = mock[GridSquareInfoFactory]
    when(factory.getByGridRef(any[GridRefDef])).thenReturn(gsi)

    val record = mock[NbnRecord]
    when(record.gridReferenceRaw).thenReturn(Some(knownGridRef_1000m))
    when(record.gridReferencePrecision).thenReturn(Some(100))

    val v = new Nbnv90Validator(factory)
    val results = v.validate(record)
    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should warn but not error if target precision is less then 100 and grid ref precision is 100") {
    val gsi = mock[GridSquareInfo]
    when(gsi.gridReferencePrecision).thenReturn(100)

    val factory = mock[GridSquareInfoFactory]
    when(factory.getByGridRef(any[GridRefDef])).thenReturn(gsi)

    val record = mock[NbnRecord]
    when(record.gridReferenceRaw).thenReturn(Some(knownGridRef_100m))
    when(record.gridReferencePrecision).thenReturn(Some(50))

    val v = new Nbnv90Validator(factory)
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be ('empty)

    results.find(r => r.level == ResultLevel.WARN) should not be ('empty)
  }

  test("should warn and error if target precision is less then 100 and grid ref precision is greater than 100") {
    val gsi = mock[GridSquareInfo]
    when(gsi.gridReferencePrecision).thenReturn(1000)

    val factory = mock[GridSquareInfoFactory]
    when(factory.getByGridRef(any[GridRefDef])).thenReturn(gsi)

    val record = mock[NbnRecord]
    when(record.gridReferenceRaw).thenReturn(Some(knownGridRef_1000m))
    when(record.gridReferencePrecision).thenReturn(Some(50))

    val v = new Nbnv90Validator(factory)
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)

    results.find(r => r.level == ResultLevel.WARN) should not be ('empty)
  }

  test("should error if precision is lower then 10,000m") {
    val factory = mock[GridSquareInfoFactory]
    val record = mock[NbnRecord]
    when(record.gridReferenceRaw).thenReturn(Some(knownGridRef_1000m))
    when(record.gridReferencePrecision).thenReturn(Some(10001))

    val v = new Nbnv90Validator(factory)
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }
}
