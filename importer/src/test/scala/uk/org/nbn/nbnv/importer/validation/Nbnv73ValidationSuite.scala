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
class Nbnv73ValidationSuite extends BaseFunSuite with BeforeAndAfter {
  val validator = new Nbnv73Validator
  var record: NbnRecord = _

  before {
    record = mock[NbnRecord]
  }

  test("Should validate when start/end date are the same") {
    when(record.dateType).thenReturn("D")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012"))

    val r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should not validate when start / end date are not the same") {
    when(record.dateType).thenReturn("D")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("17/10/2012"))

    val r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate a record with a type other than 'D'") {
    when(record.dateType).thenReturn("DD")

    val r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }
}
