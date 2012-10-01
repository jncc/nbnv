package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv81ValidatorSuite extends BaseFunSuite{
  test("should validate irish grid ref") {
    val gridRef = "A1663471237"
    val v = new Nbnv81Validator()
    val r = v.validate(gridRef, "testKey")

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate irish dinty grid ref") {
    val gridRef = "C23I"
    val v = new Nbnv81Validator()
    val r = v.validate(gridRef, "testKey")

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate british grid ref") {
    val gridRef = "NN166712"
    val v = new Nbnv81Validator()
    val r = v.validate(gridRef, "testKey")

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate british dinty grid ref") {
    val gridRef = "NN17Q"
    val v = new Nbnv81Validator()
    val r = v.validate(gridRef, "testKey")

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate ci grid ref") {
    val gridRef = "WV1671"
    val v = new Nbnv81Validator()
    val r = v.validate(gridRef, "testKey")

    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate ci dinty grid ref") {
    val gridRef = "WV17Q"
    val v = new Nbnv81Validator()
    val r = v.validate(gridRef, "testKey")

    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate malformed grid ref") {
    val gridRef = "Rubbish123"
    val v = new Nbnv81Validator()
    val r = v.validate(gridRef, "testKey")

    r.level should be (ResultLevel.ERROR)
  }
}
