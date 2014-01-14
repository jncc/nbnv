package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.{ResultLevel, Result}

class Nbnv78ValidatorSuite extends BaseFunSuite {
  val validator = new Nbnv78Validator
  val record = mock[NbnRecord]

  test("Should validate a correct DateType") {
    when(record.dateType).thenReturn(Some("D"))
    var r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)

    when(record.dateType).thenReturn(Some("DD"))
    r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)

    when(record.dateType).thenReturn(Some("O"))
    r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)

    when(record.dateType).thenReturn(Some("OO"))
    r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)

    when(record.dateType).thenReturn(Some("Y"))
    r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)

    when(record.dateType).thenReturn(Some("YY"))
    r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should not validate an invalid DateType") {
    when(record.dateType).thenReturn(Some("X"))
    var r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate if no date type is supplied") {
    when(record.dateType).thenReturn(None)
    var r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }
}
