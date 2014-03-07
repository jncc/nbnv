package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv194ValidatorSuite extends BaseFunSuite with BeforeAndAfter {
  var record : NbnRecord = _

  before {
    record = mock[NbnRecord]
    when(record.key).thenReturn("1")
    when(record.dateType).thenReturn(Some("<D"))
  }

  test("should validate a valid end date that specifies a day") {
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("12/11/2012"))
    when(record.endDate).thenReturn("12/11/2012".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv194Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate if no end date is supplied") {
    when(record.startDate).thenReturn(None)
    when(record.endDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(None)

    val v = new Nbnv194Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate if a start date is supplied") {
    when(record.startDate).thenReturn("12/11/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDate).thenReturn("12/11/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("12/11/2012"))

    val v = new Nbnv194Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate if too vague a date is given") {
    when(record.startDate).thenReturn(None)
    when(record.endDate).thenReturn("11 2012".maybeDate("MM yyyy"))
    when(record.endDateRaw).thenReturn(Some("11 2012"))

    val v = new Nbnv194Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate end dates in the future") {
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("12/11/9999"))
    when(record.endDate).thenReturn("12/11/9999".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv194Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }
}
