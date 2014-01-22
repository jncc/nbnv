package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.metadata.{Mode, Metadata}
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv899ValidatorSuite extends BaseFunSuite with BeforeAndAfter {
  var metadata: Metadata = _
  var v : Nbnv899Validator = _

  before {
    metadata = mock[Metadata]
    v = new Nbnv899Validator
  }

  test("should validate a valid importType value") {
    when(metadata.importType).thenReturn(Some(Mode.append))

    val r = v.validate(metadata)
    r.level should be (ResultLevel.DEBUG)
  }

  test("should not validate a missing result level") {
    when(metadata.importType).thenReturn(None)

    val r = v.validate(metadata)
    r.level should be (ResultLevel.ERROR)
  }

}
