package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import uk.org.nbn.nbnv.importer.records.NbnRecord
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel

class Nbnv66ValidatorSuite extends BaseFunSuite {
  test("Nvnv66 should validate when null") {
    val record = mock[NbnRecord]
    when(record.absenceText).thenReturn(null)

    val v = new Nbnv66Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv66 should validate when absenceText is presence") {
    val record = mock[NbnRecord]
    when(record.absenceText).thenReturn("Presence")

    val v = new Nbnv66Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv66 should validate when absenceText is absence") {
    val record = mock[NbnRecord]
    when(record.absenceText).thenReturn("Absence")

    val v = new Nbnv66Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.DEBUG)
  }

  test("Nvnv66 should not validate when absenceText is not null presence or absence") {
    val record = mock[NbnRecord]
    when(record.absenceText).thenReturn("asdfsdfsdf")

    val v = new Nbnv66Validator
    val r = v.validate(record)

    r.level should be (ResultLevel.ERROR)
  }
}
