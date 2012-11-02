package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import uk.org.nbn.nbnv.importer.fidelity.{Result, ResultLevel}

class Nbnv76ValidatorSuite extends BaseFunSuite with BeforeAndAfter {

  var record: NbnRecord = _

  before {
    record = mock[NbnRecord]
    when(record.key).thenReturn("1")
  }

  test("should validate if the end date supplied is the end of the year") {
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("31/12/1997"))
    when(record.endDate).thenReturn("31/12/1997".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv76Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should validate if the end date is just a year") {
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("1997"))
    when(record.endDate).thenReturn("1997".maybeDate("yyyy"))

    val v = new Nbnv76Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate if a start date is given") {
    when(record.startDate).thenReturn("01/01/1997".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/1997"))
    when(record.endDate).thenReturn("31/12/1997".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv76Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate if the end date is not the end of the year") {
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("30/12/1997"))
    when(record.endDate).thenReturn("30/12/1997".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv76Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }
}
