package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import uk.org.nbn.nbnv.importer.utility.StringParsing._

class Nbnv77ValidatorSuite extends BaseFunSuite {
  val record = mock[NbnRecord]
  val validator = new Nbnv77Validator

  test("Should allow a record to have blank dates") {
    when(record.startDate).thenReturn(None)
    when(record.endDate).thenReturn(None)

    val r = validator.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }

  test("Should not allow a record to have a start date") {
    when(record.endDate).thenReturn(None)
    when(record.startDate).thenReturn("01/01/1997".maybeDate("dd/MM/yyyy"))

    val r = validator.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should not be ('empty)
  }

  test("Should allow a record to have an end date") {
    when(record.startDate).thenReturn(None)
    when(record.endDate).thenReturn("01/01/1997".maybeDate("dd/MM/yyyy"))

    val r = validator.validate(record)

    r.find(r => r.level == ResultLevel.ERROR) should be ('empty)
  }
}
