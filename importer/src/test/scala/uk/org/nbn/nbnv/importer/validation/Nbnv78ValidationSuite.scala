package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv78ValidationSuite extends BaseFunSuite {
  val validator = new Nbnv78Validator
  val record = mock[NbnRecord]

  test("Should validate a correct DateType") {
    when(record.dateType).thenReturn("D")
    var r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)

    when(record.dateType).thenReturn("DD")
    r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)

    when(record.dateType).thenReturn("O")
    r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)

    when(record.dateType).thenReturn("OO")
    r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)

    when(record.dateType).thenReturn("Y")
    r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)

    when(record.dateType).thenReturn("YY")
    r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should not validate an invalid DateType") {
    when(record.dateType).thenReturn("X")
    var r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }
}
