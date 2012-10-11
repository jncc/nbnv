package uk.org.nbn.nbnv.importer.Recorder

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import org.mockito.Mockito._
import uk.org.nbn.nbnv.importer.records.NbnRecord
import uk.org.nbn.nbnv.importer.ImportFailedException
import uk.org.nbn.nbnv.importer.darwin.StarRecordExtensions
import org.gbif.dwc.terms.DwcTerm
import org.gbif.dwc.text.StarRecord
import org.gbif.dwc.record.Record
import java.util

/**
 * Created with IntelliJ IDEA.
 * User: Matt Debont
 * Date: 10/10/12
 * Time: 16:11
 * To change this template use File | Settings | File Templates.
 */
class NbnRecordSuite extends BaseFunSuite with BeforeAndAfter {
  var record: NbnRecord = _

  before {
    val rec = mock[Record]
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

    val starRec = mock[StarRecord]
    when(starRec.core()).thenReturn(rec)

    val rec1 = mock[Record]
    when(rec1.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("19/07/2001")

    val rec1a = mock[Record]
    when(rec1a.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("19/07/2001")

    val rec2 = mock[Record]
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")).thenReturn("D")

    val rec3 = mock[Record]
    when(rec3.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/sensitiveOccurrence")).thenReturn("false")

    val rec4 = mock[Record]
    when(rec4.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType")).thenReturn("OSGB")

    val rec5 = mock[Record]
    when(rec5.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference")).thenReturn("SK632634")

    val rec6 = mock[Record]
    when(rec6.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecisionRaw")).thenReturn("100")

    val rec7 = mock[Record]
    when(rec7.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/featureKey")).thenReturn("")

    var recs = new util.LinkedList[Record]()
    recs.add(rec1)
    recs.add(rec1a)
    recs.add(rec2)
    recs.add(rec3)
    recs.add(rec4)
    recs.add(rec5)
    recs.add(rec6)
    recs.add(rec7)

    when(starRec.extension(("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence"))).thenReturn(recs)

    record = new NbnRecord(starRec)
  }


  test("Should parse occurence statuses correctly") {
    record.parseOccurrenceStatus("presence") should be (false)
    record.parseOccurrenceStatus(null) should be (false)
    record.parseOccurrenceStatus("absence") should be (true)

    val throws = intercept[ImportFailedException] {
      record.parseOccurrenceStatus("false") should be (false)
    }
    throws.getMessage should be ("Invalid occurrence status 'false'")
  }

  test("Should parse sensitive occurences correctly") {
    record.parseSensitiveOccurrence("false") should be (false)
    record.parseSensitiveOccurrence("true") should be (true)
    record.parseSensitiveOccurrence(null) should be (false)
  }

  test("Should create a valid precission") {
    record.parseGridRefPrecision("100") should be (100)
    record.parseGridRefPrecision(null) should be (0)
    record.parseGridRefPrecision("") should be (0)
  }

  test("Should generate a valid attribute map") {
    record.attributes should have size (4)
  }
}
