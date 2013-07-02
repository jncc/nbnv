package uk.org.nbn.nbnv.importer.records

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.BadDataException
import org.gbif.dwc.terms.DwcTerm
import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.record.Record
import java.util
import java.text.SimpleDateFormat

class NbnRecordSuite extends BaseFunSuite with BeforeAndAfter {
  var record: NbnRecord = _
  val rec = mock[Record]
  val ext = mock[Record]

  before {
    when(rec.value(DwcTerm.occurrenceID)).thenReturn("1")
    when(rec.value(DwcTerm.occurrenceStatus)).thenReturn("presence")
    when(rec.value(DwcTerm.collectionCode)).thenReturn("CI0000030000000A")
    when(rec.value(DwcTerm.eventID)).thenReturn("CI000003000003T2")
    when(rec.value(DwcTerm.taxonID)).thenReturn("NHMSYS0020528265")
    when(rec.value(DwcTerm.locationID)).thenReturn("CI0000030000034W")
    when(rec.value(DwcTerm.locality)).thenReturn("Sherwood Forest Holiday Village: lake 5")
    when(rec.value(DwcTerm.recordedBy)).thenReturn("Robert Merritt")
    when(rec.value(DwcTerm.identifiedBy)).thenReturn("Robert Merritt")
    when(rec.value(DwcTerm.eventDate)).thenReturn("19/07/2001")
    when(rec.value(DwcTerm.verbatimLongitude)).thenReturn(null)
    when(rec.value(DwcTerm.verbatimLatitude)).thenReturn(null)
    when(rec.value(DwcTerm.verbatimSRS)).thenReturn(null)

    when(rec.value(DwcTerm.dynamicProperties)).thenReturn("{\"TaxonName\":\"Hippeutis complanatus\",\"Abundance\":\"\",\"Comment\":\"\",\"SampleMethod\":\"Field Observation\"}")

    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("19/07/2001")
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("19/07/2001")
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")).thenReturn("D")
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/sensitiveOccurrence")).thenReturn("false")
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType")).thenReturn("OSGB")
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference")).thenReturn("SK632634")
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecisionRaw")).thenReturn("100")
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/featureKey")).thenReturn("")

    record = createRecord
  }

  def createRecord = {
    val starRec = mock[StarRecord]
    when(starRec.core()).thenReturn(rec)

    var recs = new util.LinkedList[Record]()
    recs.add(ext)

    when(starRec.extension(("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence"))).thenReturn(recs)

    new NbnRecord(starRec)
  }


  test("Should parse occurence statuses correctly") {
    record.parseOccurrenceStatus("presence") should be (false)
    record.parseOccurrenceStatus("absence") should be (true)

    val throws = intercept[BadDataException] {
      record.parseOccurrenceStatus("false") should be (false)
    }
    throws.getMessage should be ("Invalid occurrence status 'false'")
  }

  test("Should generate a valid attribute map") {
    record.attributes should have size (4)
  }

  test("Should correctly parse dates") {
    record.startDate should be (Some(new SimpleDateFormat("dd/MM/yyyy").parse("19/07/2001")))
    record.endDate should be (Some(new SimpleDateFormat("dd/MM/yyyy").parse("19/07/2001")))

    when(rec.value(DwcTerm.eventDate)).thenReturn("19/07/2001")
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn(null)
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn(null)
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")).thenReturn(null)

    record = createRecord

    record.startDate should be (Some(new SimpleDateFormat("dd/MM/yyyy").parse("19/07/2001")))
    record.endDate should be (None)
    record.dateType should be ("D")

    when(rec.value(DwcTerm.eventDate)).thenReturn("19/07/2001")
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("16/10/2012")
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("16/10/2012")
    record = createRecord

    record.startDate should be (Some(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012")))
    record.endDate should be (Some(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012")))

    when(rec.value(DwcTerm.eventDate)).thenReturn(null)
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("16/10/2012")
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("16/10/2012")
    record = createRecord

    record.startDate should be (Some(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012")))
    record.endDate should be (Some(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012")))

    when(rec.value(DwcTerm.eventDate)).thenReturn(null)
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn(null)
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("16/10/2012")
    record = createRecord

    record.startDate should be (None)
    record.endDate should be (Some(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012")))

    when(rec.value(DwcTerm.eventDate)).thenReturn(null)
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("16/10/2012")
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn(null)
    record = createRecord

    record.startDate should be (Some(new SimpleDateFormat("dd/MM/yyyy").parse("16/10/2012")))
    record.endDate should be (None)

    when(rec.value(DwcTerm.eventDate)).thenReturn(null)
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn(null)
    when(ext.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn(null)
    record = createRecord

    record.startDate should be (None)
    record.endDate should be (None)
  }
}