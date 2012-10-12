package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import org.mockito.Mockito._
import org.gbif.dwc.terms.DwcTerm
import org.gbif.dwc.text.{ArchiveFile, Archive, StarRecord}
import org.gbif.dwc.record.Record
import java.util
import org.apache.log4j.{Level, Logger}
import uk.org.nbn.nbnv.importer.data.Repository
import uk.org.nbn.nbnv.importer.logging.Log

class ValidatorSuite extends BaseFunSuite with BeforeAndAfter{
  var archive: Archive = _

  before {
    val starRec1 = mock[StarRecord]
    val coreArchive1 = mock[Record]
    when(coreArchive1.value(DwcTerm.occurrenceID)).thenReturn("CI00000300000TNL")
    when(coreArchive1.value(DwcTerm.occurrenceStatus)).thenReturn("presence")
    when(coreArchive1.value(DwcTerm.collectionCode)).thenReturn("CI0000030000000A")
    when(coreArchive1.value(DwcTerm.eventID)).thenReturn("CI000003000003T2")
    when(coreArchive1.value(DwcTerm.taxonID)).thenReturn("NHMSYS0020528265")
    when(coreArchive1.value(DwcTerm.locationID)).thenReturn("CI0000030000034W")
    when(coreArchive1.value(DwcTerm.locality)).thenReturn("Sherwood Forest Holiday Village: lake 5")
    when(coreArchive1.value(DwcTerm.recordedBy)).thenReturn("Robert Merritt")
    when(coreArchive1.value(DwcTerm.identifiedBy)).thenReturn("Robert Merritt")
    when(coreArchive1.value(DwcTerm.eventDate)).thenReturn("19/07/2001")
    when(coreArchive1.value(DwcTerm.verbatimLongitude)).thenReturn(null)
    when(coreArchive1.value(DwcTerm.verbatimLatitude)).thenReturn(null)
    when(coreArchive1.value(DwcTerm.verbatimSRS)).thenReturn(null)
    when(coreArchive1.value(DwcTerm.dynamicProperties)).thenReturn("{\"TaxonName\":\"Hippeutis complanatus\",\"Abundance\":\"\",\"Comment\":\"\",\"SampleMethod\":\"Field Observation\"}")
    when(starRec1.core()).thenReturn(coreArchive1)

    var rec = mock[Record]
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("19/07/2001")
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("19/07/2001")
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")).thenReturn("D")
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/sensitiveOccurrence")).thenReturn("false")
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType")).thenReturn("OSGB")
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference")).thenReturn("SK632634")
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecisionRaw")).thenReturn("100")

    var recs = new util.LinkedList[Record]()
    recs.add(rec)
    when(starRec1.extension(("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence"))).thenReturn(recs)

    val starRec2 = mock[StarRecord]
    val coreArchive2 = mock[Record]
    when(coreArchive2.value(DwcTerm.occurrenceID)).thenReturn("CI00000300000TNM")
    when(coreArchive2.value(DwcTerm.occurrenceStatus)).thenReturn("presence")
    when(coreArchive2.value(DwcTerm.collectionCode)).thenReturn("CI0000030000000A")
    when(coreArchive2.value(DwcTerm.eventID)).thenReturn("CI000003000003T2")
    when(coreArchive2.value(DwcTerm.taxonID)).thenReturn("NHMSYS0020528265")
    when(coreArchive2.value(DwcTerm.locationID)).thenReturn("CI0000030000034W")
    when(coreArchive2.value(DwcTerm.locality)).thenReturn("Sherwood Forest Holiday Village: lake 5")
    when(coreArchive2.value(DwcTerm.recordedBy)).thenReturn("Robert Merritt")
    when(coreArchive2.value(DwcTerm.identifiedBy)).thenReturn("Robert Merritt")
    when(coreArchive2.value(DwcTerm.eventDate)).thenReturn("19/07/2001")
    when(coreArchive2.value(DwcTerm.verbatimLongitude)).thenReturn(null)
    when(coreArchive2.value(DwcTerm.verbatimLatitude)).thenReturn(null)
    when(coreArchive2.value(DwcTerm.verbatimSRS)).thenReturn(null)
    when(coreArchive2.value(DwcTerm.dynamicProperties)).thenReturn("{\"TaxonName\":\"Hippeutis complanatus\",\"Abundance\":\"\",\"Comment\":\"\",\"SampleMethod\":\"Field Observation\"}")
    when(starRec2.core()).thenReturn(coreArchive2)

    when(starRec2.extension(("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence"))).thenReturn(recs)

    val r = starRec2.extension("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence")

    archive = mock[Archive]
    val it = mock[org.gbif.utils.file.ClosableIterator[StarRecord]]
    when(it.next).thenReturn(starRec1).thenReturn(starRec2)
    when(archive.iteratorRaw).thenReturn(it)
  }

  test("Should Validate a couple of records - Set of 2 Records") {
    val repo = mock[Repository]
    when(repo.confirmTaxonVersionKey("NHMSYS0020528265")).thenReturn(true)
    val log : Logger = Log.get()
    Log.configure(".", "2MB", Level.WARN)
    val validator = new Validator(log, repo)
    validator.validate(archive)
  }

  test("Should validate a set of real records - Head Record Only") {
    val headVal = new ArchiveHeadValidator
    headVal.validate(archive)
  }

}
