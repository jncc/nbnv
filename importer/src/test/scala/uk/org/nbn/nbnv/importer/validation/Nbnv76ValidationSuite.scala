package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import java.text.SimpleDateFormat
import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}

/**
 * Created By: Matt Debont
 * Date: 18/10/12
 */
class Nbnv76ValidationSuite extends BaseFunSuite with BeforeAndAfter {
  val validator = new Nbnv76Validator
  var record: NbnRecord = _

  before {
    record = mock[NbnRecord]
  }

  test("Should validate a valid date format for -Y") {
    when(record.dateType).thenReturn("-Y")
    when(record.startDate).thenReturn(null)
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2012"))

    val r = validator.validate(record)
    r.level should be (ResultLevel.DEBUG)
  }

  test("Should not validate an invalid date format for -Y") {
    when(record.dateType).thenReturn("-Y")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2012"))

    var r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)

    when(record.startDate).thenReturn(null)
    when(record.endDate).thenReturn(null)

    r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)

    when(record.startDate).thenReturn(null)
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2012"))

    r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }

  test("Should not validate another date type other than -Y") {
    when(record.dateType).thenReturn("D")
    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2012"))
    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2012"))

    val r = validator.validate(record)
    r.level should be (ResultLevel.ERROR)
  }
}
