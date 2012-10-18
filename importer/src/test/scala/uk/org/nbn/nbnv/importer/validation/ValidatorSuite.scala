package uk.org.nbn.nbnv.importer.validation

import uk.org.nbn.nbnv.importer.testing.BaseFunSuite
import org.scalatest.BeforeAndAfter
import org.mockito.Mockito._
import org.gbif.dwc.terms.DwcTerm
import org.gbif.dwc.text.{ArchiveFile, Archive, StarRecord}
import org.gbif.dwc.record.Record
import java.util
import org.apache.log4j.Logger
import uk.org.nbn.nbnv.importer.data.{Database, Repository}
import scala.collection.JavaConversions._
import uk.org.nbn.nbnv.importer.ImportFailedException
import org.gbif.utils.file.ClosableIterator
import uk.org.nbn.nbnv.importer.utility.extClosableIterator
import uk.org.nbn.nbnv.importer.spatial.GridSquareInfoFactory

class ValidatorSuite extends BaseFunSuite with BeforeAndAfter{
  var archive: Archive = _
  var starRec1: StarRecord = _
  var coreArchive1: Record = _
  var rec: Record = _
  var rec2: Record = _
  var recs: util.LinkedList[Record] = _
  var recs2: util.LinkedList[Record] = _
  var starRec2: StarRecord = _
  var coreArchive2: Record = _
  val repo = mock[Repository]
  val db = mock[Database]
  val log : Logger = mock[Logger]


  before {
    starRec1 = mock[StarRecord]
    coreArchive1 = mock[Record]
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

    rec = mock[Record]
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("19/07/2001")
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("19/07/2001")
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")).thenReturn("D")
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/sensitiveOccurrence")).thenReturn("false")
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType")).thenReturn("OSGB")
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference")).thenReturn("SK632634")
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecisionRaw")).thenReturn("100")
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/featureKey")).thenReturn(null)

    recs = new util.LinkedList[Record]()
    recs.add(rec)
    when(starRec1.extension(("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence"))).thenReturn(recs)

    starRec2 = mock[StarRecord]
    coreArchive2 = mock[Record]
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

    archive = mock[Archive]

    val list = new util.ArrayList[StarRecord]
    // Added twice as mocked objected wont recreate an iterator on the fly so the first option always gets gobbled up
    // by the ArchiveHeadValidator :(
    list.add(starRec1)
    list.add(starRec1)
    list.add(starRec2)

    when(archive.iteratorRaw).thenReturn(new extClosableIterator[StarRecord](list))

    when(db.repo).thenReturn(repo)
    when(repo.confirmTaxonVersionKey("NHMSYS0020528265")).thenReturn(true)
  }

  test("Should validate a set of real records - Head Record Only") {
    val headVal = new ArchiveHeadValidator
    headVal.validate(archive)
  }

  test("Should Validate a couple of records - Set of 2 Records") {
    val validator = new Validator(log, db)
    validator.validate(archive)
  }

  test("Should validate a valid record but error on a missing id") {
    // Need to wait for a validation system for this to be available
    when(coreArchive2.value(DwcTerm.occurrenceID)).thenReturn("")

    val validator = new Validator(log, db)
    val throws = intercept[ImportFailedException] {
      validator.validate(archive)
    }
    throws should not be (null)
  }

  test("Should not validate a duplicated id") {
    when(coreArchive2.value(DwcTerm.occurrenceID)).thenReturn("CI00000300000TNL")
    val validator = new Validator(log, db)
    val throws = intercept[ImportFailedException] {
      validator.validate(archive)
    }
    throws should not be (null)
  }

  test("Should not validate an incorrect or missing location") {
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference")).thenReturn(null)
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType")).thenReturn(null)
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecisionRaw")).thenReturn(null)

    val validator = new Validator(log, db)
    val throws = intercept[ImportFailedException] {
      validator.validate(archive)
    }
    throws should not be (null)
  }

  test("Should validate with a valid feature key") {
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference")).thenReturn(null)
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType")).thenReturn(null)
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecisionRaw")).thenReturn(null)

    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/featureKey")).thenReturn("DS123456PK123")
    when(repo.confirmSiteBoundary("DS123456","PK123")).thenReturn(true)

    val validator = new Validator(log, db)
    validator.validate(archive)
  }

  test("Should validate with a valid lat/long val") {
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference")).thenReturn(null)
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType")).thenReturn(null)
    when(rec.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecisionRaw")).thenReturn(null)

    when(coreArchive1.value(DwcTerm.verbatimLongitude)).thenReturn("-0.22174100")
    when(coreArchive1.value(DwcTerm.verbatimLatitude)).thenReturn("52.585667")
    when(coreArchive1.value(DwcTerm.verbatimSRS)).thenReturn("4326")

    when(coreArchive2.value(DwcTerm.verbatimLongitude)).thenReturn("-0.22174100")
    when(coreArchive2.value(DwcTerm.verbatimLatitude)).thenReturn("52.585667")
    when(coreArchive2.value(DwcTerm.verbatimSRS)).thenReturn("4326")

    val validator = new Validator(log, db)
    validator.validate(archive)
  }

  test("Should validate records with valid locations lat long") {
    rec2 = mock[Record]
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("19/07/2001")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("19/07/2001")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")).thenReturn("D")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/sensitiveOccurrence")).thenReturn("false")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferenceType")).thenReturn(null)
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReference")).thenReturn(null)
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/gridReferencePrecisionRaw")).thenReturn(null)
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/featureKey")).thenReturn(null)

    recs2 = new util.LinkedList[Record]
    recs2.add(rec2)

    when(starRec2.extension("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/nxfOccurrence")).thenReturn(recs2)
    when(coreArchive2.value(DwcTerm.verbatimLongitude)).thenReturn("-0.22174100")
    when(coreArchive2.value(DwcTerm.verbatimLatitude)).thenReturn("52.585667")
    when(coreArchive2.value(DwcTerm.verbatimSRS)).thenReturn("4326")

    val validator = new Validator(log, db)
    validator.validate(archive)
  }

  test("Should validate records with valid datesets D / DD") {
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("19/07/2001")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("20/07/2001")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")).thenReturn("DD")

    val validator = new Validator(log, db)
    validator.validate(archive)
  }

  test("Should validate records with valid datesets D / O") {
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("01/10/2001")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("31/10/2001")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")).thenReturn("O")

    val validator = new Validator(log, db)
    validator.validate(archive)
  }

  test("Should validate records with valid datesets D / OO") {
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("01/10/2001")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("31/12/2001")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")).thenReturn("OO")

    val validator = new Validator(log, db)
    validator.validate(archive)
  }

  test("Should validate records with valid datesets D / P") {
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("01/10/2001")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("31/12/2001")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")).thenReturn("P")

    val validator = new Validator(log, db)
    validator.validate(archive)
  }

  test("Should validate records with valid datesets D / Y") {
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("01/01/2001")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("31/12/2001")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")).thenReturn("Y")

    val validator = new Validator(log, db)
    validator.validate(archive)
  }

  test("Should validate records with valid datesets D / YY") {
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateStart")).thenReturn("01/01/2001")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateEnd")).thenReturn("31/12/2012")
    when(rec2.value("http://rs.nbn.org.uk/dwc/nxf/0.1/terms/eventDateTypeCode")).thenReturn("YY")

    val validator = new Validator(log, db)
    validator.validate(archive)
  }
}
