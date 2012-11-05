package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import scala.Some
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.utility.StringParsing._

class Nbnv217ValidatorSuite extends BaseFunSuite with BeforeAndAfter {
  var record : NbnRecord = _

  before {
    record = mock[NbnRecord]
    when(record.key).thenReturn("1")
  }

  test("should validate a start date that specifies a day") {
    when(record.endDate).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("12/11/2012"))
    when(record.startDate).thenReturn("12/11/2012".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv217Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate if no start date is supplied") {
    when(record.endDate).thenReturn(None)
    when(record.startDate).thenReturn(None)
    when(record.startDateRaw).thenReturn(None)

    val v = new Nbnv217Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate if an end date is supplied") {
    when(record.endDate).thenReturn("12/11/2012".maybeDate("dd/MM/yyyy"))
    when(record.startDate).thenReturn("12/11/2012".maybeDate("dd/MM/yyyy"))
    when(record.startDateRaw).thenReturn(Some("12/11/2012"))

    val v = new Nbnv217Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate if too vague a date is given") {
    when(record.endDate).thenReturn(None)
    when(record.startDate).thenReturn("11 2012".maybeDate("MM yyyy"))
    when(record.startDateRaw).thenReturn(Some("11 2012"))

    val v = new Nbnv217Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

}
