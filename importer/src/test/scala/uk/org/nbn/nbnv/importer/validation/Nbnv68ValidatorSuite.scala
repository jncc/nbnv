package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import java.text.SimpleDateFormat
import uk.org.nbn.nbnv.importer.BadDataException
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import scala.Some
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import org.scalatest.BeforeAndAfter

class Nbnv68ValidatorSuite extends BaseFunSuite with BeforeAndAfter {

  var record : NbnRecord = _

  before {
    record = mock[NbnRecord]
  }

  test("should not validate a start date that is too vague") {
    when(record.startDateRaw).thenReturn(Some("21/08/11"))

    when(record.endDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be (None)
  }

  test("should not validate an end date that is too vague") {
    when(record.endDateRaw).thenReturn(Some("21/08/11"))

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be (None)
  }

  test("should not validate an event date that is too vague") {
    val record = mock[NbnRecord]
    when(record.eventDateRaw).thenReturn(Some("21/08/11"))

    when(record.startDateRaw).thenReturn(None)

    when(record.endDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be (None)
  }

  test("should validate an end date that conforms to dd/MM/yyyy") {
    when(record.endDateRaw).thenReturn(Some("21/08/2011"))

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to dd/MM/yyyy") {
    when(record.startDateRaw).thenReturn(Some("21/08/2011"))

    when(record.endDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an event date that conforms to dd/MM/yyyy") {
    when(record.startDateRaw).thenReturn(None)

    when(record.endDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(Some("21/08/2011"))

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to dd-MM-yyyy") {
    when(record.startDateRaw).thenReturn(Some("21-08-2011"))

    when(record.endDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to dd-MM-yyyy") {
    when(record.endDateRaw).thenReturn(Some("21-08-2011"))

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an event date that conforms to dd-MM-yyyy") {
    when(record.endDateRaw).thenReturn(None)

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(Some("21-08-2011"))

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to yyyy/MM/dd") {
    when(record.startDateRaw).thenReturn(Some("2011/08/21"))

    when(record.endDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to yyyy/MM/dd") {

    when(record.endDateRaw).thenReturn(Some("2011/08/21"))

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an event date that conforms to yyyy/MM/dd") {

    when(record.endDateRaw).thenReturn(None)

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(Some("2011/08/21"))

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to yyyy-MM-dd") {
    when(record.startDateRaw).thenReturn(Some("2011-08-21"))

    when(record.endDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to yyyy-MM-dd") {
    when(record.endDateRaw).thenReturn(Some("2011-08-21"))

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an event date that conforms to yyyy-MM-dd") {
    when(record.endDateRaw).thenReturn(None)

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(Some("2011-08-21"))

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to dd MMM yyyy") {
    when(record.startDateRaw).thenReturn(Some("21 Aug 2011"))

    when(record.endDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to dd MMM yyyy") {
    when(record.endDateRaw).thenReturn(Some("21 Aug 2011"))

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an event date that conforms to dd MMM yyyy") {
    when(record.endDateRaw).thenReturn(None)

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(Some("21 Aug 2011"))

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to dd/MMM/yyyy") {
    when(record.startDateRaw).thenReturn(Some("21/Aug/2011"))

    when(record.endDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to dd/MMM/yyyy") {
    when(record.endDateRaw).thenReturn(Some("21/Aug/2011"))

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an event date that conforms to dd/MMM/yyyy") {
    when(record.endDateRaw).thenReturn(None)

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(Some("21/Aug/2011"))

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to dd-MMM-yyyy") {
    when(record.startDateRaw).thenReturn(Some("21-Aug-2011"))

    when(record.endDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to dd-MMM-yyyy") {
    when(record.endDateRaw).thenReturn(Some("21-Aug-2011"))

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an event date that conforms to dd-MMM-yyyy") {
    when(record.endDateRaw).thenReturn(None)

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(Some("21-Aug-2011"))

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to MMM yyyy") {
    when(record.startDateRaw).thenReturn(Some("Aug 2011"))

    when(record.endDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to MMM yyyy") {
    when(record.endDateRaw).thenReturn(Some("Aug 2011"))

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to yyyy") {
    when(record.startDateRaw).thenReturn(Some("2011"))

    when(record.endDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to yyyy") {
    when(record.endDateRaw).thenReturn(Some("2011"))

    when(record.startDateRaw).thenReturn(None)

    when(record.eventDateRaw).thenReturn(None)

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }
}
