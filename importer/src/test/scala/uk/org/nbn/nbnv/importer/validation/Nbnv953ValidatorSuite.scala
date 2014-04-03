package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.metadata.Metadata
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv953ValidatorSuite extends BaseFunSuite with BeforeAndAfter {

  var validator: Nbnv953Validator = _
  var metadata: Metadata = _

  before {
    validator = new Nbnv953Validator
    metadata = mock[Metadata]
  }

  test("should validate email less then 70 char") {
    when(metadata.administratorEmail).thenReturn("a" * 69)

    val r = validator.validate(metadata)
    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate a blank email") {
    when(metadata.administratorEmail).thenReturn("")

    val r = validator.validate(metadata)
    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate a null email") {
    when(metadata.administratorEmail).thenReturn(null)

    val r = validator.validate(metadata)
    r.level should be (ResultLevel.DEBUG)
  }

  test("should validate an email of exactly 70 char") {
    when(metadata.administratorEmail).thenReturn("a" * 70)

    val r = validator.validate(metadata)
    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate an email over 70 char") {
    when(metadata.administratorEmail).thenReturn("a" * 71)

    val r = validator.validate(metadata)
    r.level should be (ResultLevel.ERROR)
  }


}
