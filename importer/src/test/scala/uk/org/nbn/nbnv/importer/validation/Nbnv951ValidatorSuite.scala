package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.metadata.Metadata
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv951ValidatorSuite extends BaseFunSuite with BeforeAndAfter {
  var metadata: Metadata = _
  var validator: Nbnv951Validator = _

  before {
    metadata = mock[Metadata]
    validator = new Nbnv951Validator
  }

  test("should valdate a title less then 200 char") {
    when(metadata.datasetTitle).thenReturn("a" * 199)

    val r = validator.validate(metadata)
    r.level should be (ResultLevel.DEBUG)
  }

  test("should valdate a title of 200 char") {
    when(metadata.datasetTitle).thenReturn("a" * 200)

    val r = validator.validate(metadata)
    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate a title of more then 200 char") {
    when(metadata.datasetTitle).thenReturn("a" * 201)

    val r = validator.validate(metadata)
    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate a blank title") {
    when(metadata.datasetTitle).thenReturn("")

    val r = validator.validate(metadata)
    r.level should be (ResultLevel.ERROR)
  }

  test("should not validate a null title") {
    when(metadata.datasetTitle).thenReturn(null)

    val r = validator.validate(metadata)
    r.level should be (ResultLevel.ERROR)
  }

}
