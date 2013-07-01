package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.utility.StringParsing._
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv73ValidatorSuite extends BaseFunSuite {

//
  test("Should validate if only a start date is given") {
    val record = mock[NbnRecord]
    val startDateRaw = "03/08/2011"
    when(record.startDateRaw).thenReturn(Some(startDateRaw))
    when(record.startDate).thenReturn(startDateRaw.maybeDate("dd/MM/yyyy"))
    when(record.endDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(None)
    when(record.dateType).thenReturn("D")

    val v = new Nbnv73Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("Should validate when start/end date are the same") {
    val record = mock[NbnRecord]
    val startDateRaw = "03/08/2011"
    when(record.startDateRaw).thenReturn(Some(startDateRaw))
    when(record.startDate).thenReturn(startDateRaw.maybeDate("dd/MM/yyyy"))
    when(record.endDateRaw).thenReturn(Some(startDateRaw))
    when(record.endDate).thenReturn(startDateRaw.maybeDate("dd/MM/yyyy"))
    when(record.dateType).thenReturn("D")


    val v = new Nbnv73Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should be (None)
  }

  test("Should not validate when start / end date are not the same") {
    val record = mock[NbnRecord]
    val startDateRaw = "03/08/2011"
    when(record.startDateRaw).thenReturn(Some(startDateRaw))
    when(record.startDate).thenReturn(startDateRaw.maybeDate("dd/MM/yyyy"))

    val endDateRaw = "05/08/2011"
    when(record.endDateRaw).thenReturn(Some(endDateRaw))
    when(record.endDate).thenReturn(endDateRaw.maybeDate("dd/MM/yyyy"))

    when(record.dateType).thenReturn("D")

    val v = new Nbnv73Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be (None)
  }

  test("Should not validate a start date that is too vague") {
    val record = mock[NbnRecord]
    val startDateRaw = "08 2011"
    when(record.startDateRaw).thenReturn(Some(startDateRaw))
    when(record.startDate).thenReturn(startDateRaw.maybeDate("MM yyyy"))
    when(record.endDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(None)
    when(record.dateType).thenReturn("D")

    val v = new Nbnv73Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be (None)
  }

  test("Should not validate a start date year that is too vague") {
    val record = mock[NbnRecord]
    val startDateRaw = "21/08/11"
    when(record.startDateRaw).thenReturn(Some(startDateRaw))
    when(record.startDate).thenReturn(startDateRaw.maybeDate("dd/MM/yy"))

    when(record.endDate).thenReturn(None)
    when(record.endDateRaw).thenReturn(None)

    when(record.dateType).thenReturn("D")

    val v = new Nbnv73Validator
    val results = v.validate(record)

    results.find(r => r.level == ResultLevel.ERROR) should not be (None)
  }

}
