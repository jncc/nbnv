package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import uk.org.nbn.nbnv.importer.records.NbnRecord
import java.text.SimpleDateFormat
import uk.org.nbn.nbnv.importer.fidelity.ResultLevel
import org.mockito.Mockito._


class Nbnv71ValidatorSuite extends BaseFunSuite with BeforeAndAfter {


//
//  test("Should validate if all dates are in the past") {
//    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012"))
//    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("15/10/2012"))
//    when(record.eventDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("14/10/2012"))
//
//    var r = validator.validate(record)
//    r.level should be (ResultLevel.DEBUG)
//
//    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012"))
//    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("15/10/2012"))
//    when(record.eventDate).thenReturn(null)
//
//    r = validator.validate(record)
//    r.level should be (ResultLevel.DEBUG)
//
//    when(record.startDate).thenReturn(null)
//    when(record.endDate).thenReturn(null)
//    when(record.eventDate).thenReturn(null)
//
//    r = validator.validate(record)
//    r.level should be (ResultLevel.DEBUG)
//  }
//
//  test("We should not validate if any of the dates are in the future") {
//    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/3012"))
//    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("15/10/2012"))
//    when(record.eventDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("14/10/2012"))
//
//    var r = validator.validate(record)
//    r.level should be (ResultLevel.ERROR)
//
//    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012"))
//    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("15/10/3012"))
//    when(record.eventDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("14/10/2012"))
//
//    r = validator.validate(record)
//    r.level should be (ResultLevel.ERROR)
//
//    when(record.startDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012"))
//    when(record.endDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("15/10/2012"))
//    when(record.eventDate).thenReturn(new SimpleDateFormat("dd/MM/yyyy").parse("14/10/3012"))
//
//    r = validator.validate(record)
//    r.level should be (ResultLevel.ERROR)
//  }

}
