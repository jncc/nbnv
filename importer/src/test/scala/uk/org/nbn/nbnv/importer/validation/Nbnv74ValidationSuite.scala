package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import java.text.SimpleDateFormat
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import org.scalatest.BeforeAndAfter

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 */
class Nbnv74ValidationSuite extends BaseFunSuite with BeforeAndAfter {
  val validator = new Nbnv74Validator
  var record: NbnRecord = _

  before {
    record = mock[NbnRecord]
  }

  test("Should validate a valid O type date set") {
    when(record.dateType).thenReturn("O")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("01/10/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2012"))

    val r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should validate a valid OO type date set") {
    when(record.dateType).thenReturn("OO")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("01/10/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("30/11/2012"))

    val r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should not validate an invalid O type date set") {
    when(record.dateType).thenReturn("O")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("01/10/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("30/11/2012"))

    var r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)

    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("01/10/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("09/10/2012"))

    r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)

    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("02/10/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2012"))

    r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate an invalid OO type date set") {
    when(record.dateType).thenReturn("OO")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("01/10/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("09/10/2012"))

    var r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)

    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("02/10/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2012"))

    r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate a non O / OO type date set") {
    when(record.dateType).thenReturn("D")

    val r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }
}
