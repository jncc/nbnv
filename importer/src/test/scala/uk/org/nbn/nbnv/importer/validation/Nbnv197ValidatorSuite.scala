package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.utility.StringParsing._

class Nbnv197ValidatorSuite extends BaseFunSuite {
  test("should validate a start date and end date of the same year") {
    val record = mock[NbnRecord]
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2011"))
    when(record.startDate).thenReturn("01/01/2011".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/2011"))
    when(record.endDate).thenReturn("31/12/2011".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv197Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should validate a start date and end date of different years") {
    val record = mock[NbnRecord]
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2011"))
    when(record.startDate).thenReturn("01/01/2011".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/2014"))
    when(record.endDate).thenReturn("31/12/2014".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv197Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should validate a start date and end date of a leap year"){
    val record = mock[NbnRecord]
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2012"))
    when(record.startDate).thenReturn("01/01/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/2012"))
    when(record.endDate).thenReturn("31/12/2012".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv197Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("should not validate a start date but no end date"){
    val record = mock[NbnRecord]
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2012"))
    when(record.startDate).thenReturn("01/01/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    val v = new Nbnv197Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate an end date but no start date"){
    val record = mock[NbnRecord]
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(Some("31/12/2012"))
    when(record.endDate).thenReturn("31/12/2012".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv197Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate a start date that is not the start of the year") {
    val record = mock[NbnRecord]
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("21/01/2012"))
    when(record.startDate).thenReturn("21/01/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("31/12/2012"))
    when(record.endDate).thenReturn("31/12/2012".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv197Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("should not validate an end date that is not the end of the year") {
    val record = mock[NbnRecord]
    when(record.eventDateRaw).thenReturn(None)
    when(record.startDateRaw).thenReturn(Some("01/01/2012"))
    when(record.startDate).thenReturn("01/01/2012".maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some("09/12/2012"))
    when(record.endDate).thenReturn("09/12/2012".maybeDate("dd/MM/yyyy"))

    val v = new Nbnv197Validator
    val r = v.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

}
