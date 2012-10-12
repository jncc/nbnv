package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv159ValidatorSuite extends BaseFunSuite {
  val knownUkGridRef = "NN166712"
  val knownIrishGridRef = "A166712"
  val knownCIGridRef = "WV166712"
  val badGridRef = "gibber"
  val recordKey = "1"

  test("Should validate if no gridref type supplied") {
    val v = new Nbnv159Validator()
    val r = v.validate(knownUkGridRef, None, recordKey)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Should validate gb grid ref and OSGB36 type") {

    val v = new Nbnv159Validator()
    val r = v.validate(knownUkGridRef, Option("OSGB36"), recordKey)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Should validate irish grid ref and OSNI type") {
    val v = new Nbnv159Validator()
    val r = v.validate(knownIrishGridRef, Option("OSNI"), recordKey)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Should validate ci grid ref and ED50 type") {

    val v = new Nbnv159Validator()
    val r = v.validate(knownCIGridRef, Option("ED50"), recordKey)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Should not validate bad grid ref and OSGB36 type") {

    val v = new Nbnv159Validator()
    val r = v.validate(badGridRef, Option("OSGB36"), recordKey)

    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate bad grid ref and OSNI type") {

    val v = new Nbnv159Validator()
    val r = v.validate(badGridRef, Option("OSNI"), recordKey)

    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate bad grid ref and ED50 type") {

    val v = new Nbnv159Validator()
    val r = v.validate(badGridRef, Option("ED50"), recordKey)

    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate unknown grid ref type") {

    val v = new Nbnv159Validator()
    val r = v.validate(knownUkGridRef, Option("GARBAGE_GR_TYPE"), recordKey)

    r.level should be (ResultLevel.ERROR)
  }

}
