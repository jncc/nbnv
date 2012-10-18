package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import java.util.Date

/**
 * Created By: Matt Debont
 * Date: 18/10/12
 */
class Nbnv77ValidationSuite extends BaseFunSuite with BeforeAndAfter {
  val validator: Nbnv77Validator = new Nbnv77Validator
  var record: NbnRecord = _

  before{
    record = mock[NbnRecord]
  }

  test("Should validate a valid ND or U record") {
    when(record.dateType).thenReturn("ND")
    when(record.startDate).thenReturn(null)
    when(record.startDate).thenReturn(null)

    var r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)

    when(record.dateType).thenReturn("U")

    r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should not validate an invalid ND or U record") {
    when(record.dateType).thenReturn("ND")
    when(record.startDate).thenReturn(new Date())
    when(record.endDate).thenReturn(new Date())

    var r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)

    when(record.dateType).thenReturn("U")

    r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)

    when(record.dateType).thenReturn("ND")
    when(record.startDate).thenReturn(new Date())
    when(record.endDate).thenReturn(null)

    r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)

    when(record.dateType).thenReturn("U")

    r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)

    when(record.dateType).thenReturn("ND")
    when(record.startDate).thenReturn(null)
    when(record.endDate).thenReturn(new Date())

    r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)

    when(record.dateType).thenReturn("U")

    r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate a non ND or U record") {
    when(record.dateType).thenReturn("DD")

    val r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }

}
