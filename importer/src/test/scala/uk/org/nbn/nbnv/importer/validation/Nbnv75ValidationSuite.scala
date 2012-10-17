package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import java.text.SimpleDateFormat
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

/**
 * Created By: Matt Debont
 * Date: 17/10/12
 */
class Nbnv75ValidationSuite extends BaseFunSuite with BeforeAndAfter {
  val validator = new Nbnv75Validator
  var record: NbnRecord = _

  before {
    record = mock[NbnRecord]
  }

  test("Should validate a valid Y record") {
    when(record.dateType).thenReturn("Y")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2012"))

    val r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should validate a valid YY record") {
    when(record.dateType).thenReturn("YY")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2014"))

    val r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should not validate an invalid Y record") {
    when(record.dateType).thenReturn("Y")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2014"))

    var r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)

    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2012"))

    r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate an invalid YY record") {
    when(record.dateType).thenReturn("Y")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2014"))

    val r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate a non Y / YY record") {
    when(record.dateType).thenReturn("OO")

    val r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }

}
