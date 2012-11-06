package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv75ValidatorSuite extends BaseFunSuite with BeforeAndAfter {
  var record: NbnRecord = _

  before {
    record = mock[NbnRecord]
    when(record.key).thenReturn("1")
  }

  test("should validate if the start date supplied is the start of the year") {
    when(record.endDate).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/1997"))
    when(record.startDate).thenReturn("01/01/1997".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv75Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should validate if the start date is just a year") {
    when(record.endDate).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("1997"))
    when(record.startDate).thenReturn("1997".maybeDate("yyyy"))

    val v = new Nbnv75Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate if a end date is given") {
    when(record.endDate).thenReturn("31/12/1997".maybeDate("dd/MM/yyyy"))
    when(record.startDateRaw).thenReturn(Some("01/01/1997"))
    when(record.startDate).thenReturn("01/01/1997".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv75Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate if the start date is not the start of the year") {
    when(record.endDate).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("03/01/1997"))
    when(record.startDate).thenReturn("03/01/1997".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv76Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }
}
