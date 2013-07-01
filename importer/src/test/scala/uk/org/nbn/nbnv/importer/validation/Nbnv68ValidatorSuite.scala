package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import java.text.SimpleDateFormat
import uk.org.nbn.nbnv.importer.BadDataException
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import scala.Some
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.utility.StringParsing._

class Nbnv68ValidatorSuite extends BaseFunSuite {

  test("should not validate a start date that is too vague") {
    val record = mock[NbnRecord]
    val startDateRaw = "21/08/11"
    when(record.startDateRaw).thenReturn(Some(startDateRaw))
    when(record.startDate).thenReturn(startDateRaw.maybeDate("dd/MM/yy"))

    when(record.endDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be (None)
  }

  test("should not validate an end date that is too vague") {
    val record = mock[NbnRecord]
    val endDateRaw = "21/08/11"
    when(record.endDateRaw).thenReturn(Some(endDateRaw))
    when(record.endDate).thenReturn(endDateRaw.maybeDate("dd/MM/yy"))

    when(record.startDate).thenReturn(None)
    when(record.startDateRaw).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be (None)
  }

  test("should validate an end date that conforms to dd/MM/yyyy") {
    val record = mock[NbnRecord]
    val endDateRaw = "21/08/2011"
    when(record.endDateRaw).thenReturn(Some(endDateRaw))
    when(record.endDate).thenReturn(endDateRaw.maybeDate("dd/MM/yyyy"))

    when(record.startDate).thenReturn(None)
    when(record.startDateRaw).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to dd/MM/yyyy") {
    val record = mock[NbnRecord]
    val rawDate = "21/08/2011"
    when(record.startDateRaw).thenReturn(Some(rawDate))
    when(record.startDate).thenReturn(rawDate.maybeDate("dd/MM/yyyy"))

    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to dd-MM-yyyy") {
    val record = mock[NbnRecord]
    val rawDate = "21-08-2011"
    when(record.startDateRaw).thenReturn(Some(rawDate))
    when(record.startDate).thenReturn(rawDate.maybeDate("dd-MM-yyyy"))

    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to dd-MM-yyyy") {
    val record = mock[NbnRecord]
    val rawDate = "21-08-2011"
    when(record.endDateRaw).thenReturn(Some(rawDate))
    when(record.endDate).thenReturn(rawDate.maybeDate("dd-MM-yyyy"))

    when(record.startDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to yyyy/MM/dd") {
    val record = mock[NbnRecord]
    val rawDate = "2011/08/21"
    when(record.startDateRaw).thenReturn(Some(rawDate))
    when(record.startDate).thenReturn(rawDate.maybeDate("yyyy/MM/dd"))

    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to yyyy/MM/dd") {
    val record = mock[NbnRecord]
    val rawDate = "2011/08/21"
    when(record.endDateRaw).thenReturn(Some(rawDate))
    when(record.endDate).thenReturn(rawDate.maybeDate("yyyy/MM/dd"))

    when(record.startDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to yyyy-MM-dd") {
    val record = mock[NbnRecord]
    val rawDate = "2011-08-21"
    when(record.startDateRaw).thenReturn(Some(rawDate))
    when(record.startDate).thenReturn(rawDate.maybeDate("yyyy-MM-dd"))

    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to yyyy-MM-dd") {
    val record = mock[NbnRecord]
    val rawDate = "2011-08-21"
    when(record.endDateRaw).thenReturn(Some(rawDate))
    when(record.endDate).thenReturn(rawDate.maybeDate("yyyy-MM-dd"))

    when(record.startDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to dd MMM yyyy") {
    val record = mock[NbnRecord]
    val rawDate = "21 Aug 2011"
    when(record.startDateRaw).thenReturn(Some(rawDate))
    when(record.startDate).thenReturn(rawDate.maybeDate("dd MMM yyyy"))

    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to dd MMM yyyy") {
    val record = mock[NbnRecord]
    val rawDate = "21 Aug 2011"
    when(record.endDateRaw).thenReturn(Some(rawDate))
    when(record.endDate).thenReturn(rawDate.maybeDate("dd MMM yyyy"))

    when(record.startDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate a start date that conforms to MMM yyyy") {
    val record = mock[NbnRecord]
    val rawDate = "Aug 2011"
    when(record.startDateRaw).thenReturn(Some(rawDate))
    when(record.startDate).thenReturn(rawDate.maybeDate("MMM yyyy"))

    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to MMM yyyy") {
    val record = mock[NbnRecord]
    val rawDate = "Aug 2011"
    when(record.endDateRaw).thenReturn(Some(rawDate))
    when(record.endDate).thenReturn(rawDate.maybeDate("MMM yyyy"))

    when(record.startDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }


  test("should validate a start date that conforms to yyyy") {
    val record = mock[NbnRecord]
    val rawDate = "2011"
    when(record.startDateRaw).thenReturn(Some(rawDate))
    when(record.startDate).thenReturn(rawDate.maybeDate("yyyy"))

    when(record.endDateRaw).thenReturn(None)
    when(record.endDate).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("should validate an end date that conforms to yyyy") {
    val record = mock[NbnRecord]
    val rawDate = "2011"
    when(record.endDateRaw).thenReturn(Some(rawDate))
    when(record.endDate).thenReturn(rawDate.maybeDate("yyyy"))

    when(record.startDateRaw).thenReturn(None)
    when(record.startDate).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv68Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }
}
