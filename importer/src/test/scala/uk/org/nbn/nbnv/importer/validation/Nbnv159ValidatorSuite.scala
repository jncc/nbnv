package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.scalatest.BeforeAndAfter

class Nbnv159ValidatorSuite extends BaseFunSuite with BeforeAndAfter {
  var record : NbnRecord = _

  val knownUkGridRef = Some("NN166712")
  val ukGridType = Some("OSGB36")
  val ukSRS = Some(27700)
  val knownIrishGridRef = Some("A166712")
  val irishGridType = Some("OSNI")
  val irishSRS = Some(29903)
  val knownCIGridRef = Some("WV166712")
  val ciGridType = Some("ED50")
  val ciSRS = Some(23030)
  val badGridRef = Some("gibber")

  before {
    record = mock[NbnRecord]
    when(record.key).thenReturn("1")
  }

  test("Should validate if no gridref type supplied") {
    when(record.gridReferenceRaw).thenReturn(knownUkGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(None)
    when(record.srs).thenReturn(None)

    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Should validate gb grid ref and OSGB36 type") {
    when(record.gridReferenceRaw).thenReturn(knownUkGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(ukGridType)
    when(record.srs).thenReturn(None)

    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Should validate gb grid ref and 27700 srs") {
    when(record.gridReferenceRaw).thenReturn(knownUkGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(None)
    when(record.srs).thenReturn(ukSRS)

    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Should validate irish grid ref and OSNI type") {
    when(record.gridReferenceRaw).thenReturn(knownIrishGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(irishGridType)
    when(record.srs).thenReturn(None)

    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Should validate irish grid ref and 29903 srs") {
    when(record.gridReferenceRaw).thenReturn(knownIrishGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(None)
    when(record.srs).thenReturn(irishSRS)

    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Should validate ci grid ref and ED50 type") {
    when(record.gridReferenceRaw).thenReturn(knownCIGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(ciGridType)
    when(record.srs).thenReturn(None)

    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Should validate ci grid ref and 23030 srs") {
    when(record.gridReferenceRaw).thenReturn(knownCIGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(None)
    when(record.srs).thenReturn(ciSRS)

    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }


  test("Should not validate bad grid ref and OSGB36 type") {
    when(record.gridReferenceRaw).thenReturn(badGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(ukGridType)
    when(record.srs).thenReturn(None)

    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate bad grid ref and 27700 srs") {
    when(record.gridReferenceRaw).thenReturn(badGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(None)
    when(record.srs).thenReturn(ukSRS)

    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate bad grid ref and OSNI type") {
    when(record.gridReferenceRaw).thenReturn(badGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(irishGridType)
    when(record.srs).thenReturn(None)

    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate bad grid ref and 29903 srs") {
    when(record.gridReferenceRaw).thenReturn(badGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(None)
    when(record.srs).thenReturn(irishSRS)

    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate bad grid ref and ED50 type") {
    when(record.gridReferenceRaw).thenReturn(badGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(ciGridType)
    when(record.srs).thenReturn(None)

    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate bad grid ref and 23030 srs") {
    when(record.gridReferenceRaw).thenReturn(badGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(None)
    when(record.srs).thenReturn(ciSRS)

    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate unknown grid ref type") {
    when(record.gridReferenceRaw).thenReturn(knownUkGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(Option("GARBAGE_GR_TYPE"))
    when(record.srs).thenReturn(None)


    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate an srs that does not map to a grid type") {
    when(record.gridReferenceRaw).thenReturn(knownUkGridRef)
    when(record.gridReferenceTypeRaw).thenReturn(None)
    when(record.srs).thenReturn(Option(4326))


    val v = new Nbnv159Validator()
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }

}
